package com.dao;

import com.entity.Club;
import com.utils.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClubDao {

    // 插入时带上 userId
    public void add(Club club) {
        String sql = "insert into club(title,time,content,has_cert,has_volunteer,has_prize,userId) values(?,?,?,?,?,?,?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, club.getTitle());
            pstmt.setString(2, club.getTime());
            pstmt.setString(3, club.getContent());
            pstmt.setString(4, club.getHas_cert());
            pstmt.setString(5, club.getHas_volunteer());
            pstmt.setString(6, club.getHas_prize());
            pstmt.setInt(7, club.getUserId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 查询时带上 userId
    public List<Club> listAll() {
        List<Club> list = new ArrayList<>();
        String sql = "select * from club";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Club c = new Club();
                c.setId(rs.getInt("id"));
                c.setTitle(rs.getString("title"));
                c.setTime(rs.getString("time"));
                c.setContent(rs.getString("content"));
                c.setHas_cert(rs.getString("has_cert"));
                c.setHas_volunteer(rs.getString("has_volunteer"));
                c.setHas_prize(rs.getString("has_prize"));
                c.setUserId(rs.getInt("userId")); // 加上
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void delete(int id) {
        String sql = "delete from club where id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 根据ID查询，必须返回 userId
    public Club findById(int id) {
        Club club = null;
        String sql = "select * from club where id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                club = new Club();
                club.setId(rs.getInt("id"));
                club.setTitle(rs.getString("title"));
                club.setTime(rs.getString("time"));
                club.setContent(rs.getString("content"));
                club.setHas_cert(rs.getString("has_cert"));
                club.setHas_volunteer(rs.getString("has_volunteer"));
                club.setHas_prize(rs.getString("has_prize"));
                club.setUserId(rs.getInt("userId")); // 加上
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return club;
    }

    public void update(Club club) {
        String sql = "update club set title=?,time=?,content=?,has_cert=?,has_volunteer=?,has_prize=? where id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, club.getTitle());
            pstmt.setString(2, club.getTime());
            pstmt.setString(3, club.getContent());
            pstmt.setString(4, club.getHas_cert());
            pstmt.setString(5, club.getHas_volunteer());
            pstmt.setString(6, club.getHas_prize());
            pstmt.setInt(7, club.getId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}