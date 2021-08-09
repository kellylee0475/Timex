package com.te.timex.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.timex.model.Board;


public interface BoardRepository extends JpaRepository<Board, Integer> {
	
	
	List<Board> findByTitle(String title);
	List<Board> findByTitleOrContent(String title, String content);
	List<Board> findByTitleAndContent(String title, String content);
}