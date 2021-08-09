
package com.te.timex.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.te.timex.model.Board;
import com.te.timex.repository.BoardRepository;


@Controller
@RequestMapping("/board")
public class BoardController{

	@Autowired
	private BoardRepository boardRepository;
	
	@GetMapping("/list")
	public String list(Model model) {
		List<Board> boards = boardRepository.findAll();//table data 가져온다

		model.addAttribute("boards", boards);
		return "board/list";//login 페이지로 이동
	}
	
	@GetMapping("/form")//requestParam => list.html에서 "@{/board/form(id=${board.id})}" 로 넘겨주는 값 
	public String form(Model model, @RequestParam(required=false) int id) {
	
		if(id==0) {
			model.addAttribute("board",new Board());
		}else {
			Board board = boardRepository.findById(id).orElse(null);//key 값 기준으로 찾을수있는 메소드
			model.addAttribute("board", board);
		}

		return "board/form";//login 페이지로 이동
	}
	
	@PostMapping("/form")
	public String formSubmit(@Valid Board board, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "board/form";
		}
		
		boardRepository.save(board);//id key값이 있을경우는 update, 없을경우는 create
		return "redirect:/board/list";
	}
	
}