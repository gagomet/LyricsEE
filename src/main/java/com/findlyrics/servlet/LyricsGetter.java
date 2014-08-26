package com.findlyrics.servlet;

import com.findlyrics.db.service.ILyricService;
import com.findlyrics.db.service.impl.DBLyricsService;
import com.findlyrics.dto.LyricItemDTO;
import com.findlyrics.dto.LyricsDTO;
import com.findlyrics.exceptions.DbConnectionException;
import com.findlyrics.rest.service.RestLyricsService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Padonag on 25.08.2014.
 */

@WebServlet("/search")
public class LyricsGetter extends HttpServlet {

    String query;
    LyricsDTO resultDTO;

    private void getDto(String query) {
        resultDTO = new LyricsDTO();
        ILyricService dbService = new DBLyricsService();

        try {
            resultDTO = dbService.getDTO(query);
        } catch (DbConnectionException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        query = request.getParameter("query");
        getDto(query);
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter pw = response.getWriter();
        pw.println("<h1>OLOLO</h1>");
        pw.println("<h2>your query is </h2>" + query);

        for(LyricItemDTO dto : resultDTO.getSearchResults()){
            pw.println(dto.getArtistName() + " " + dto.getSongName()+" </br>");
        }

    }
}
