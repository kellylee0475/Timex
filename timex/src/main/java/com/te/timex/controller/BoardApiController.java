//package com.te.timex.controller;
//
//import java.util.List;
//
//import org.apache.groovy.parser.antlr4.util.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.te.timex.model.Board;
//import com.te.timex.repository.BoardRepository;
//
//@RestController
//@RequestMapping("/api")
//public class BoardApiController {
//	
//  @Autowired
//  private BoardRepository repository;
//
//  //title,content 두가지 조건으로 찾기
//  @GetMapping("/boards")
//  List<Board> all(@RequestParam(required=false, defaultValue="") String title, @RequestParam(required=false, defaultValue="") String content) {
//	  if(StringUtils.isEmpty(title)) {
//		   return repository.findAll();
//	  }else {
//		  
//		  return repository.findByTitleOrContent(title, content);
//	  }
// 
//  }
//  //title만으로 찾기
////  @GetMapping("/boards")
////  List<Board> all(@RequestParam(required=false) String title) {
////	  if(StringUtils.isEmpty(title)) {
////		   return repository.findAll();
////	  }else {
////		  
////		  return repository.findByTitle(title);
////	  }
//// 
////  }
//  // end::get-aggregate-root[]
//
//  @PostMapping("/boards")
//  Board newBoard(@RequestBody Board newBoard) {
//    return repository.save(newBoard);
//  }
//
//  // Single item
//  
//  @GetMapping("/boards/{id}")
//  Board one(@PathVariable int id) {
//    
//    return repository.findById(id).orElse(null);
//  }
//
//  @PutMapping("/boards/{id}")
//  Board replaceBoard(@RequestBody Board newBoard, @PathVariable int id) {
//    
//    return repository.findById(id)
//      .map(Board -> {
//        Board.setTitle(newBoard.getTitle());
//        Board.setContent(newBoard.getContent());
//        return repository.save(Board);
//      })
//      .orElseGet(() -> {
//        newBoard.setId(id);
//        return repository.save(newBoard);
//      });
//  }
//
//  @DeleteMapping("/boards/{id}")
//  void deleteBoard(@PathVariable int id) {
//    repository.deleteById(id);
//  }
//}