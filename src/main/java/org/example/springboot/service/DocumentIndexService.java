package org.example.springboot.service;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentIndexService {
    private static final String INDEX_PATH = "F:/dachuang/source/index";
    private final Directory directory;
    private final Analyzer analyzer;

    public DocumentIndexService() throws IOException {
        this.directory = FSDirectory.open(Paths.get(INDEX_PATH));
        this.analyzer = new SmartChineseAnalyzer();
    }

    public void indexDocuments() throws IOException {
        IndexWriter writer = new IndexWriter(directory,
                new IndexWriterConfig(analyzer));

        File docsDir = new File("F:/dachuang/source/documents");
        for (File file : docsDir.listFiles()) {
            if (file.getName().endsWith(".docx")) {
                String content = extractWordContent(file);
                Document doc = new Document();
                doc.add(new TextField("filename", file.getName(), Field.Store.YES));
                doc.add(new TextField("content", content, Field.Store.YES));
                writer.addDocument(doc);
            }
        }
        writer.close();
    }

    private String extractWordContent(File file) throws IOException {
        try (XWPFDocument document = new XWPFDocument(
                new FileInputStream(file))) {
            StringBuilder content = new StringBuilder();
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                content.append(paragraph.getText()).append("\n");
            }
            return content.toString();
        }
    }

    public List<String> searchRelevantContent(String query) throws IOException {
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        QueryParser parser = new QueryParser("content", analyzer);

        try {
            Query searchQuery = parser.parse(query);
            TopDocs results = searcher.search(searchQuery, 3);

            List<String> relevantContent = new ArrayList<>();
            for (ScoreDoc scoreDoc : results.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                relevantContent.add(doc.get("content"));
            }
            return relevantContent;
        } catch (ParseException e) {
            throw new IOException("查询解析失败", e);
        } finally {
            reader.close();
        }
    }

    private static final String DOCS_DIR = "F:/dachuang/source/documents";
}