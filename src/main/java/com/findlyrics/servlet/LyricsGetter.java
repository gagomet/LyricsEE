package com.findlyrics.servlet;

import com.findlyrics.db.service.ILyricService;
import com.findlyrics.db.service.impl.DBLyricsService;
import com.findlyrics.dto.LyricItemDTO;
import com.findlyrics.dto.LyricsDTO;
import com.findlyrics.exceptions.DbConnectionException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Padonag on 25.08.2014.
 */

@WebServlet("/search.do")
public class LyricsGetter extends HttpServlet {

    String query;
    LyricsDTO resultDTO;

    public LyricsGetter() {
        super();
    }

    private void getDto(String query) {
        resultDTO = new LyricsDTO();
        ILyricService dbService = new DBLyricsService();

        try {
            resultDTO = dbService.getDTO(query);
        } catch (DbConnectionException e) {
            e.printStackTrace();
        }
    }

    private List<LyricItemDTO> getPageList(int offset, int noOfRecordsPerPage) {
        List<LyricItemDTO> pageList = new LinkedList<LyricItemDTO>();
        for (int i = offset; i < offset + noOfRecordsPerPage; i++) {
            pageList.add(resultDTO.getSearchResults().get(i));
        }
        return pageList;
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int page = 1;
        int recordsPerPage = 10;
        if (request.getParameter("query") != null) {
            query = request.getParameter("query");
            getDto(query);
        }
        if (request.getParameter("page") != null)
            page = Integer.parseInt(request.getParameter("page"));

        List<LyricItemDTO> resultList = getPageList((page - 1) * recordsPerPage, recordsPerPage);
        int noOfRecords = resultDTO.getSearchResults().size();
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
        request.setAttribute("songList", resultList);
        request.setAttribute("noOfPages", noOfPages);
        request.setAttribute("currentPage", page);


//        for(int i = 0; i < resultDTO.getSearchResults().size(); i++){
//            request.setAttribute("name"+i, resultDTO.getSearchResults().get(i));
//        }

        request.getRequestDispatcher("getlyrics.jsp").forward(request, response);

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }
}
