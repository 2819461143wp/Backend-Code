package org.example.springboot.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.springboot.mapper.LikeMapper;
import org.example.springboot.mapper.PostMapper;
import org.example.springboot.pojo.Like;
import org.example.springboot.pojo.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostMapper postMapper;

    @Autowired
    private LikeMapper likeMapper;

    // 根据ID获取帖子
    public Post getPostById(Integer id) {
        return postMapper.getPostById(id);
    }

    // 新建帖子
    public boolean insertPost(Integer user_id, String title, String content, String image_url, Integer status) {
        Post post = new Post();
        post.setUserId(user_id);
        post.setTitle(title);
        post.setContent(content);
        post.setImageUrl(image_url);
        post.setStatus(status);
        // 设置创建时间和更新时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createAt = dateFormat.format(new Date());
        post.setCreatedAt(createAt);
        post.setUpdatedAt(createAt);
        int result = postMapper.insertPost(post);
        return result > 0;
    }

    // 更新帖子
    public boolean updatePost(Post post) {
        int result = postMapper.updatePost(post);
        return result > 0;
    }

    // 分页获取总帖子
    public PageInfo<Post> getAllPostsByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Post> posts = postMapper.getAllPosts();
        return new PageInfo<>(posts);
    }
    //分页获取各部分的贴子
    public PageInfo<Post> getPostsByPage(Integer pageNum, Integer pageSize,Integer status) {
        PageHelper.startPage(pageNum, pageSize);
        List<Post> posts = postMapper.getPosts(status);
        return new PageInfo<>(posts);
    }

    public PageInfo<Post> adminGetAllPosts(int pageNum, int pageSize,
                                      String title, Integer status, Integer allow) {
        PageHelper.startPage(pageNum, pageSize);
        List<Post> posts = postMapper.adminGetAllPosts(title, status, allow);
        return new PageInfo<>(posts);
    }


    public boolean updatePostAllow(Integer id, Integer allow) {
        return postMapper.updatePostAllow(id, allow) > 0;
    }

    public boolean deletePost(Integer id) {
        return postMapper.deletePost(id) > 0;
    }
}