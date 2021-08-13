package com.te.timex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.timex.model.Board;
import com.te.timex.model.User;
import com.te.timex.repository.BoardRepository;
import com.te.timex.repository.UserRepository;

@Service
public class BoardService{
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public Board save(String email, Board board) {
		User user = userRepository.findByEmail(email);
		board.setUser(user);
		return boardRepository.save(board);
	}
}