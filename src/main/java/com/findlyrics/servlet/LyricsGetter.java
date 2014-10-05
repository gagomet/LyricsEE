package com.findlyrics.servlet;

import com.findlyrics.db.dao.IArtistDAO;
import com.findlyrics.db.dao.PartialDataAccessDAO;
import com.findlyrics.db.dao.impl.ArtistDAO;
import com.findlyrics.db.model.Artist;
import com.findlyrics.db.model.Song;
import com.findlyrics.dto.LyricItemDTO;
import com.findlyrics.exceptions.DbConnectionException;

import javax.servlet.RequestDispatcher;
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

@WebServlet(urlPatterns={"/search.db"})
public class LyricsGetter extends HttpServlet {


    public LyricsGetter() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int page = 1;
        int recordsPerPage = 10;
        String query = request.getParameter("query");
        PartialDataAccessDAO songDao = new PartialDataAccessDAO();
        List<LyricItemDTO> resultList = getDto(page, recordsPerPage, query, songDao);

        int noOfRecords = songDao.getNoOfRecords();
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
        request.setAttribute("songsList", resultList);
        request.setAttribute("noOfPages", noOfPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("query", query);
        RequestDispatcher view = request.getRequestDispatcher("getlyrics.jsp");
        view.forward(request, response);

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int recordsPerPage = 10;
        String query = request.getParameter("query");
        int page = Integer.parseInt(request.getParameter("page"));
        PartialDataAccessDAO songDao = new PartialDataAccessDAO();
        List<LyricItemDTO> resultList = getDto(page, recordsPerPage, query, songDao);

        int noOfRecords = songDao.getNoOfRecords();
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
        request.setAttribute("songsList", resultList);
        request.setAttribute("noOfPages", noOfPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("query", query);
        RequestDispatcher view = request.getRequestDispatcher("getlyrics.jsp");
        view.forward(request, response);

    }
    private List<LyricItemDTO> getDto(int page, int recordsPerPage, String query, PartialDataAccessDAO songDao){

        IArtistDAO artistDAO = new ArtistDAO();
    List<Song> list = songDao.getSongsPart((page - 1) * recordsPerPage,
            recordsPerPage, query);
    List<LyricItemDTO> resultList = new LinkedList<LyricItemDTO>();
    for (Song currentSong : list) {
        try {
            Artist tempArtist = artistDAO.getArtist(currentSong.getArtistId());
            LyricItemDTO itemDTO = new LyricItemDTO(tempArtist, currentSong);
            resultList.add(itemDTO);
        } catch (DbConnectionException e) {
            e.printStackTrace();
        }
    }
        return resultList;
    }
}
