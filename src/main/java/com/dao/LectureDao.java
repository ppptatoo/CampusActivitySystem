package com.dao;

import com.entity.Lecture;
import com.utils.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LectureDao {

    // 插入时带上 userId
    public void add(Lecture lecture) {
        String sql = "insert into lecture(title,time,address,content,userId) values(?,?,?,?,?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, lecture.getTitle());
            pstmt.setString(2, lecture.getTime());
            pstmt.setString(3, lecture.getAddress());
            pstmt.setString(4, lecture.getContent());
            pstmt.setInt(5, lecture.getUserId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 查询时带上 userId
    public List<Lecture> listAll() {
        List<Lecture> list = new ArrayList<>();
        String sql = "select * from lecture";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Lecture lec = new Lecture();
                lec.setId(rs.getInt("id"));
                lec.setTitle(rs.getString("title"));
                lec.setTime(rs.getString("time"));
                lec.setAddress(rs.getString("address"));
                lec.setContent(rs.getString("content"));
                lec.setUserId(rs.getInt("userId")); // 加上
                list.add(lec);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void delete(int id) {
        String sql = "delete from lecture where id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 根据ID查询，必须返回 userId
    public Lecture findById(int id) {
        Lecture lec = null;
        String sql = "select * from lecture where id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                lec = new Lecture();
                lec.setId(rs.getInt("id"));
                lec.setTitle(rs.getString("title"));
                lec.setTime(rs.getString("time"));
                lec.setAddress(rs.getString("address"));
                lec.setContent(rs.getString("content"));
                lec.setUserId(rs.getInt("userId")); // 加上
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lec;
    }

    public void update(Lecture lecture) {
        String sql = "update lecture set title=?,time=?,address=?,content=? where id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, lecture.getTitle());
            pstmt.setString(2, lecture.getTime());
            pstmt.setString(3, lecture.getAddress());
            pstmt.setString(4, lecture.getContent());
            pstmt.setInt(5, lecture.getId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}