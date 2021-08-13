
package com.te.timex.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.te.timex.model.Board;
import com.te.timex.repository.BoardRepository;
import com.te.timex.service.BoardService;
import com.te.timex.validator.BoardValidator;


@Controller
@RequestMapping("/board")
public class BoardController{

	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private BoardValidator boardValidator;
	
	@GetMapping("/list")
	public String list(Model model,@PageableDefault(size=2) Pageable pageable, 
			@RequestParam(required=false,defaultValue="")String searchText) {
		Page<Board> boards = boardRepository.findByTitleContainingOrContentContaining(searchText, searchText, pageable);
//		Page<Board> boards = boardRepository.findAll(pageable);//table data 가져온다
//		Page<Board> boards = boardRepository.findAll(PageRequest.of(0, 20));//table data 가져온다
	//	boards.getTotalElements();//전체 데이터 건수 -> list.html에서 사용 boards.totalElements
	//	boards.getTotalPages();
		//		List<Board> boards = boardRepository.findAll();//table data 가져온다
		int startPage = Math.max(1, boards.getPageable().getPageNumber()-4);
		int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber()+4);
//	boards.getPageable().getPageNumber();//현재페이지넘버
		model.addAttribute("startPage",startPage);
		model.addAttribute("endPage",endPage);
		model.addAttribute("boards", boards);
		return "board/list";//login 페이지로 이동
	}
	
	@GetMapping("/form")//requestParam => list.html에서 "@{/board/form(id=${board.id})}" 로 넘겨주는 값 
	public String form(Model model, @RequestParam(required=false, defaultValue = "0") int id) {
		System.out.println("here! "+id);
		if(id==0) {
			System.out.println("id is null");
			model.addAttribute("board",new Board());
		}else {
			System.out.println("id is not null");

			Board board = boardRepository.findById(id).orElse(null);//key 값 기준으로 찾을수있는 메소드
			System.out.println("here2 "+board);
			model.addAttribute("board", board);
		}

		return "board/form";//login 페이지로 이동
	}
	
	@PostMapping("/form")
	public String postForm(@Valid Board board, BindingResult bindingResult, Authentication authentication) {
		System.out.println("postmapping");
		boardValidator.validate(board,bindingResult);
		if(bindingResult.hasErrors()) {
			System.out.println("has errors");
			return "board/form";
		}
	//	Authentication a = SecurityContextHolder.getContext().getAuthentication(); //다른방법
		String email = authentication.getName();//인증정보를 가져와서 사용자정보  값이 담김다
	
		boardService.save(email, board);
	//	boardRepository.save(board);//id key값이 있을경우는 update, 없을경우는 create
		return "redirect:/board/list";
	}
	
}