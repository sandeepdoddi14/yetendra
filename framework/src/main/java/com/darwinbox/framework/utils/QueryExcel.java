package com.darwinbox.framework.utils;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.darwinbox.framework.beans.Configuration;
import com.darwinbox.framework.beans.Suite;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class QueryExcel {

    private final Fillo fillo;
    private Connection connection;
    private Configuration config;
    private String query = "";

    public QueryExcel(Configuration config) {
        fillo = new Fillo();
        this.config = config;
    }

    public void createQuery(){

        List<Integer> tcIds = new ArrayList<Integer>();
        String queryString = config.getTestcases();

        if (queryString.startsWith("mod:"))
            query = "where Module = " + queryString.substring(4);
        else if (queryString.startsWith("scope:"))
            query = "where Scope = " + queryString.substring(6);
        else if (queryString.startsWith("pr:"))
            query = "where Priority = " + queryString.substring(3);
        else if (queryString.startsWith("all")) {
            query = "";
        } else {

            String[] tcidlist = queryString.split(",");

            for (String id : tcidlist) {
                if (id.contains("-")) {
                    String tcRange[] = id.split("-");
                    int low = Integer.parseInt(tcRange[0]);
                    int high = Integer.parseInt(tcRange[1]);
                    while (low <= high) {
                        tcIds.add(low++);
                    }
                    continue;
                }

                int idnum = Integer.parseInt(id);
                tcIds.add(idnum);

                String tcidStr = "";
                for (Integer tcId : tcIds ){
                    tcidStr = tcidStr +","+ tcId;
                }
                query = "where TCID in ("+tcidStr.substring(1)+")" ;
            }
        }
    }

    public void createSuite() {
        try {
            query = "Select * from TestCaseMaster " + query;
            connection = fillo.getConnection(config.getTestDataFile());
            Recordset recordset = connection.executeQuery(query);
            createSuite(recordset);
        } catch (FilloException e) {
            e.printStackTrace();
        } finally {
        	connection.close();
        }
    }

    private void createSuite(Recordset recordset) {
       }

}
