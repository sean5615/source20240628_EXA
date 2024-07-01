<%/*
----------------------------------------------------------------------------------
File Name		: exa404r
Author			: tonny
Description		: 列印考試座次表 - 主要頁面
Modification Log	:

Vers		Date       	By            	Notes
--------------	--------------	--------------	----------------------------------
0.0.1		096/07/05	tonny    	Code Generate Create
----------------------------------------------------------------------------------
*/%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="MS950"%>
<%@ include file="/utility/header.jsp"%>
<%@ include file="/utility/titleSetup.jsp"%>
<%@page import="com.acer.db.*,com.nou.aut.*,java.util.Vector"%>
<script>
	<%
	
	
	/**取得AUTGETRANGE的單位代號*/		
	AUTGETRANGE autGetRange = (AUTGETRANGE)session.getAttribute("AUTGETRANGE");
	Vector v=autGetRange.getDEP_CODE("4","3"); // 中心行政人員
	String dep_code = "";

	if (v.size() > 0)
	{
		for(int i=0;i<v.size();i++)
		{
			if (i > 0)
				dep_code += ",";
			dep_code += "'" + (String)v.get(i) + "'";
		}
		dep_code = ("AND CENTER_CODE  IN ("+dep_code+") ");
	}
	// 如不為全校行政人員
	else if(autGetRange.getDEP_CODE("3","3").size()==0)
	{
		dep_code = ("AND CENTER_CODE  IN ('') ");
	}

	String ASYS = (String)session.getAttribute("ASYS");	

	/** 學期 */
	session.setAttribute("exa404r_01_SELECT", "NOU#SELECT CODE AS SELECT_VALUE, CODE_NAME AS SELECT_TEXT FROM SYST001 WHERE KIND='[KIND]' ORDER BY SELECT_VALUE, SELECT_TEXT ");
	session.setAttribute("exa404r_03_SELECT", "NOU#SELECT CENTER_CODE  AS SELECT_VALUE, CENTER_NAME AS SELECT_TEXT FROM SYST002 WHERE CENTER_CODE <> '00' "+dep_code+" ORDER BY SELECT_VALUE, SELECT_TEXT ");
	// 教室
	session.setAttribute("EXA404R_01_DYNASELECT", "NOU#SELECT '' as SELECT_VALUE, '請選擇' as SELECT_TEXT, '0' AS ORDER_SELECT FROM DUAL UNION SELECT TO_CHAR(a.CLSSRM_CODE)  AS SELECT_VALUE, TO_CHAR(C.CLSSRM_NAME) AS SELECT_TEXT, TO_CHAR(a.CLSSRM_CODE) AS ORDER_SELECT FROM EXAT017 a, EXAT021 b,plat003 c,SYST002 D WHERE a.AYEAR='[AYEAR]' AND   a.SMS='[SMS]' AND   a.EXAM_TYPE='[EXAM_TYPE]' AND   a.CENTER_CODE='[CENTER_CODE]' AND   a.CMPS_CODE='[CMPS_CODE]' AND   B.AYEAR = b.AYEAR AND   B.SMS = A.SMS AND   B.CRSNO = A.CRSNO AND   B.RESIT_TYPE = '1' AND   b.SECTION='[SECTION]'  AND   D.CENTER_CODE = A.CENTER_CODE  AND   C.AYEAR = A.AYEAR AND   C.SMS = A.SMS AND   C.CENTER_ABRCODE = D.CENTER_ABRCODE AND   C.CLSSRM_CODE = A.CLSSRM_CODE AND  A.CLSSRM_CODE != '000000' ORDER BY ORDER_SELECT ");

	/** 取得預設值 */
	String keyParam = "";
    	java.text.SimpleDateFormat dateTimeFormat = new java.text.SimpleDateFormat("yyyyMMdd");
	java.util.Calendar cal = java.util.Calendar.getInstance();
	String today = dateTimeFormat.format(cal.getTime());

    	com.acer.log.MyLogger logger = new com.acer.log.MyLogger("exa402r");
    	com.acer.db.DBManager dbManager = new com.acer.db.DBManager(logger);
	com.nou.sys.SYSGETSMSDATA sys = new com.nou.sys.SYSGETSMSDATA(dbManager);
	sys.setSYS_DATE(today);
	// 1.當期 2.前期 3.後期 4.前學年 5.後學年
	sys.setSMS_TYPE("1");
	int result = sys.execute();	
	if(result == 1) {
        if(!keyParam.equals("") && keyParam.length() > 0) {
            keyParam += "&AYEAR=" + sys.getAYEAR() + "&SMS=" + sys.getSMS();
        } else {
            keyParam = "?AYEAR=" + sys.getAYEAR() + "&SMS=" + sys.getSMS();
        }
        //java.util.Vector dataVt = com.acer.util.DBUtil.getSQLData("exa404r_.jsp", "NOU", "SELECT * FROM EXAT020 WHERE AYEAR = '" + sys.getAYEAR() + "' AND SMS = '" + sys.getSMS() + "' AND RESIT_TYPE = '1' AND MID_EDATE >= '" + today + "'");
        java.util.Vector dataVt = com.acer.util.DBUtil.getSQLData("exa404r_.jsp", "NOU", "SELECT * FROM EXAT020 WHERE RESIT_TYPE = '1' AND MID_EDATE >= '" + today + "'");
        com.acer.db.VtResultSet rs = new com.acer.db.VtResultSet(dataVt);
        if(rs.next()) {
            if(!keyParam.equals("") && keyParam.length() > 0) {
                keyParam += "&EXAM_TYPE=1";
            } else {
                keyParam = "?EXAM_TYPE=1";
            }
        } else {
            //dataVt = com.acer.util.DBUtil.getSQLData("exa404r_.jsp", "NOU", "SELECT * FROM EXAT020 WHERE AYEAR = '" + sys.getAYEAR() + "' AND SMS = '" + sys.getSMS() + "' AND RESIT_TYPE = '1' AND FNL_EDATE >= '" + today + "'");
            dataVt = com.acer.util.DBUtil.getSQLData("exa404r_.jsp", "NOU", "SELECT * FROM EXAT020 WHERE RESIT_TYPE = '1' AND FNL_EDATE >= '" + today + "'");
            rs	=	new com.acer.db.VtResultSet(dataVt);
            if(rs.next()) {
                if(!keyParam.equals("") && keyParam.length() > 0) {
                    keyParam += "&EXAM_TYPE=2";
                } else {
                    keyParam = "?EXAM_TYPE=2";
                }
            }
        }
	}	
                if(!keyParam.equals("") && keyParam.length() > 0) {
                    keyParam += "&ASYS=" + ASYS;
                } else {
                    keyParam = "?ASYS=" + ASYS;
                }	
				
	
	
	
	/** 傳遞 Key 參數 */
	//String	keyParam	=	com.acer.util.Utility.checkNull(request.getParameter("keyParam"), "");
	%>
	top.hideView();
	/** 導向第一個處理的頁面 */
	top.mainFrame.location.href	=	'exa404r_01v1.jsp<%=keyParam%>';
</script>