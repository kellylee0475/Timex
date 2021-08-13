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
//import com.te.timex.model.User;
//import com.te.timex.repository.UserRepository;
//
//@RestController
//@RequestMapping("/api")
//public class UserApiController {
//	
//  @Autowired
//  private UserRepository repository;
//
//  //title,content 두가지 조건으로 찾기
//  @GetMapping("/users")
//  List<User> all() {
//	  return repository.findAll();
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
//  @PostMapping("/users")
//  User newUser(@RequestBody User newUser) {
//    return repository.save(newUser);
//  }
//
//  // Single item
//  
//  @GetMapping("/users/{id}")
//  User one(@PathVariable int id) {
//    
//    return repository.findById(id).orElse(null);
//  }
//
//  @PutMapping("/users/{id}")
//  User replaceUser(@RequestBody User newUser, @PathVariable int id) {
//    
//    return repository.findById(id)
//      .map(User -> {
//    	  User.setBoards(newUser.getBoards());
//    	  for(Board board: User.getBoards()) {
//    		  board.setUser(User);
//    	  }
//    //	  User.setTitle(newBoard.getTitle());
//    //	  User.setContent(newBoard.getContent());
//        return repository.save(User);
//      })
//      .orElseGet(() -> {
//        newUser.setId(id);
//        return repository.save(newUser);
//      });
//  }
//
//  @DeleteMapping("/users/{id}")
//  void deleteUser(@PathVariable int id) {
//    repository.deleteById(id);
//  }
//}