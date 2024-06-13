package com.example.jhproject.controller;

import com.example.jhproject.dto.Board;
import com.example.jhproject.dto.LoginInfo;
import com.example.jhproject.service.BoardService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/")
    public String list(@RequestParam(name="page", defaultValue = "1") int page, HttpSession session, Model model){
        // 게시물 목록을 읽어온다. 페이징 처리한다.
        LoginInfo loginInfo = (LoginInfo)session.getAttribute("loginInfo");
        model.addAttribute("loginInfo", loginInfo);

        int totalCount = boardService.getTotalCount();
        List<Board> list = boardService.getBoards(page); // page가 1,2,3,4,...
        int pageCount = totalCount / 10;
        if (totalCount % 10 > 0) { // 나머지가 있을 경우 1page를 추가
            pageCount++;
        }
        int currentPage = page;

        model.addAttribute("list", list);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("currentPage", currentPage);
        return "list";
    }


    @GetMapping("/board")
    public String board(@RequestParam("boardId") int boardId, Model model){
        System.out.println("boardId : " + boardId);


        Board board = boardService.getBoard(boardId);
        model.addAttribute("board", board);
        return "board";
    }



    @GetMapping("/writeForm")
    public String writeForm(HttpSession session, Model model){

        LoginInfo loginInfo = (LoginInfo)session.getAttribute("loginInfo");
        if(loginInfo == null){ //세션에 로그인 정보가 없으면 /loginform으로 redirect
            return "redirect:/loginform";
        }

        model.addAttribute("loginInfo", loginInfo);
        return "writeForm";
    }

    @PostMapping("/write")
    public String write(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            HttpSession session
    ){
        LoginInfo loginInfo = (LoginInfo)session.getAttribute("loginInfo");
        if(loginInfo == null){ //세션에 로그인 정보가 없으면 /loginform으로 redirect
            return "redirect:/loginform";
        }


        System.out.println("title : " + title);

        boardService.addBoard(loginInfo.getUserId(), title, content);
        // 로그인 한 회원 정보 + 제목, 내용을 저장한다.
        return "redirect:/";
    }

    @GetMapping("/delete")
    public String delete(
            @RequestParam("boardId") int boardId,
            HttpSession session
    ){
        LoginInfo loginInfo = (LoginInfo) session.getAttribute("loginInfo");
        if(loginInfo == null){ // 세션에 로그인 정보가 없으면 /loginform으로 redirect
            return "redirect:/loginform";
        }

        // loginInfo.getUserId() 사용자가 쓴 글일 경우에만 삭제한다.
        List<String> roles = loginInfo.getRoles();
        if(roles.contains("ROLE_ADMIN")){
            boardService.deleteBoard(boardId);
        }else {
            boardService.deleteBoard(loginInfo.getUserId(), boardId);
        }
        return "redirect:/";
    }

    @GetMapping("/updateform")
    public String updateForm(@RequestParam("boardId") int boardId, Model model, HttpSession session){
        LoginInfo loginInfo = (LoginInfo) session.getAttribute("loginInfo");
        if(loginInfo == null){
            return "redirect:/loginform";
        }
        Board board = boardService.getBoard(boardId, false);
        model.addAttribute("board", board);
        model.addAttribute("loginInfo", loginInfo);
        return "updateform";
    }

    @PostMapping("/update")
    public String update(@RequestParam("boardId") int boardId,
                         @RequestParam("title") String title,
                         @RequestParam("content") String content,
                         HttpSession session){

        LoginInfo loginInfo = (LoginInfo) session.getAttribute("loginInfo");
        if(loginInfo == null){
            return "redirect:/loginform";
        }

        Board board = boardService.getBoard(boardId, false);
        if(board.getUserId() != loginInfo.getUserId()){
            return "redirect:/board?boardId=" + boardId;
        }
        boardService.updateBoard(boardId, title, content);
        return "redirect:/board?boardId=" + boardId; // 수정된 글 보기로 리다이렉트
    }
}
