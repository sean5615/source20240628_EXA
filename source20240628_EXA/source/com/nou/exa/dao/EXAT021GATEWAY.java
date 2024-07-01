package com.nou.exa.dao;

import com.acer.db.DBManager;
import com.acer.db.query.DBResult;
import com.acer.util.Utility;
import com.acer.apps.Page;
import com.nou.aut.AUTCONNECT;

import java.sql.Connection;
import java.util.*;

/*
 * (EXAT021) Gateway/*
 *-------------------------------------------------------------------------------*
 * Author    : 國長      2007/04/20
 * Modification Log :
 * Vers     Date           By             Notes
 *--------- -------------- -------------- ----------------------------------------
 * V0.0.1   2007/04/20     國長           建立程式
 *                                        新增 getExat021ForUse(Hashtable ht)
 * V0.0.2   2007/04/27     芳如           新增 getSectionAndTime(Hashtable ht)
 * V0.0.3   2007/05/04     WEN               新增 getExat021ForExamTime(Hashtable ht)
 * V0.0.4   2007/06/06     國長           新增 getEXA013M(Hashtable ht)
 * V0.0.5   2007/06/07     國榮           新增 getExat417RForExamDate(Hashtable ht)
 * V0.0.6   2007/07/10	   嘉駿	 新增 getExaNumSeat(Vector vt, Hashtable ht)
 * V0.0.6   2007/09/20 	   WEN	 修改 getExat021ForExamTime(Hashtable ht)  的SQL
 *--------------------------------------------------------------------------------
 */


public class EXAT021GATEWAY
{

    /** 資料排序方式 */
    private String orderBy = "";

    private DBManager dbmanager = null;
    private Connection conn = null;

    /* 頁數 */
    private int pageNo = 0;

    /** 每頁筆數 */
    private int pageSize = 0;

    /** 記錄是否分頁 */
    private boolean pageQuery = false;

    /** 用要存放 SQL 語法的物件 */
    private StringBuffer sql = new StringBuffer();

    /** <pre>
     *  設定資料排序方式.
     *  Ex: "AYEAR, SMS DESC"
     *      先以 AYEAR 排序再以 SMS 倒序序排序
     *  </pre>
     */
    public void setOrderBy(String orderBy) {
        if(orderBy == null) {
            orderBy = "";
        }
        this.orderBy = orderBy;
    }

    /** 取得總筆數 */
    public int getTotalRowCount() {
        return Page.getTotalRowCount();
    }

    /** 不允許建立空的物件 */
    private EXAT021GATEWAY() {}


    /** 不分頁使用 */
    public EXAT021GATEWAY(DBManager dbmanager, Connection conn)
    {
        this.dbmanager = dbmanager;
        this.conn = conn;
    }


    /** 分頁使用 */
    public EXAT021GATEWAY(DBManager dbmanager, Connection conn, int pageNo, int pageSize)
    {
        this.dbmanager = dbmanager;
        this.conn = conn;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        pageQuery = true;
    }


    /**
     * 科目考試節次資料檔
     * @param ht 條件值
     * @return 回傳 Vector 物件，內容為 Hashtable 的集合，<br>
     *         每一個 Hashtable 其 KEY 為欄位名稱，KEY 的值為欄位的值<br>
     *         若該欄位有中文名稱，則其 KEY 請加上 _NAME, EX: SMS 其中文欄位請設為 SMS_NAME
     * @throws Exception
     */
    public Vector getExat021ForUse(Hashtable ht) throws Exception
    {
        Vector result = new Vector();
        if(sql.length() > 0) {
            sql.delete(0, sql.length());
        }
        sql.append(
            "SELECT E21.AYEAR, E21.SMS, E21.RESIT_TYPE, E21.CRSNO, E21.SECTION, E21.UPD_USER_ID, E21.UPD_DATE, E21.UPD_TIME, E21.UPD_MK, E21.ROWSTAMP " +
            "FROM EXAT021 E21 " +
            "WHERE 1 = 1 "
        );
        if(!Utility.nullToSpace(ht.get("AYEAR")).equals("")) {
            sql.append("AND E21.AYEAR = '" + Utility.nullToSpace(ht.get("AYEAR")) + "' ");
        }
        if(!Utility.nullToSpace(ht.get("SMS")).equals("")) {
            sql.append("AND E21.SMS = '" + Utility.nullToSpace(ht.get("SMS")) + "' ");
        }
        if(!Utility.nullToSpace(ht.get("RESIT_TYPE")).equals("")) {
            sql.append("AND E21.RESIT_TYPE = '" + Utility.nullToSpace(ht.get("RESIT_TYPE")) + "' ");
        }
        if(!Utility.nullToSpace(ht.get("CRSNO")).equals("")) {
            sql.append("AND E21.CRSNO = '" + Utility.nullToSpace(ht.get("CRSNO")) + "' ");
        }
        if(!Utility.nullToSpace(ht.get("SECTION")).equals("")) {
            sql.append("AND E21.SECTION = '" + Utility.nullToSpace(ht.get("SECTION")) + "' ");
        }
        if(!Utility.nullToSpace(ht.get("UPD_USER_ID")).equals("")) {
            sql.append("AND E21.UPD_USER_ID = '" + Utility.nullToSpace(ht.get("UPD_USER_ID")) + "' ");
        }
        if(!Utility.nullToSpace(ht.get("UPD_DATE")).equals("")) {
            sql.append("AND E21.UPD_DATE = '" + Utility.nullToSpace(ht.get("UPD_DATE")) + "' ");
        }
        if(!Utility.nullToSpace(ht.get("UPD_TIME")).equals("")) {
            sql.append("AND E21.UPD_TIME = '" + Utility.nullToSpace(ht.get("UPD_TIME")) + "' ");
        }
        if(!Utility.nullToSpace(ht.get("UPD_MK")).equals("")) {
            sql.append("AND E21.UPD_MK = '" + Utility.nullToSpace(ht.get("UPD_MK")) + "' ");
        }
        if(!Utility.nullToSpace(ht.get("ROWSTAMP")).equals("")) {
            sql.append("AND E21.ROWSTAMP = '" + Utility.nullToSpace(ht.get("ROWSTAMP")) + "' ");
        }

        if(!orderBy.equals("")) {
            String[] orderByArray = orderBy.split(",");
            orderBy = "";
            for(int i = 0; i < orderByArray.length; i++) {
                orderByArray[i] = "E21." + orderByArray[i].trim();

                if(i == 0) {
                    orderBy += "ORBEY BY ";
                } else {
                    orderBy += ", ";
                }
                orderBy += orderByArray[i].trim();
            }
            sql.append(orderBy.toUpperCase());
            orderBy = "";
        }

        DBResult rs = null;
        try {
            if(pageQuery) {
                // 依分頁取出資料
                rs = Page.getPageResultSet(dbmanager, conn, sql.toString(), pageNo, pageSize);
            } else {
                // 取出所有資料
                rs = dbmanager.getSimpleResultSet(conn);
                rs.open();
                rs.executeQuery(sql.toString());
            }
            Hashtable rowHt = null;
            while (rs.next()) {
                rowHt = new Hashtable();
                /** 將欄位抄一份過去 */
                for (int i = 1; i <= rs.getColumnCount(); i++)
                    rowHt.put(rs.getColumnName(i), rs.getString(i));

                result.add(rowHt);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if(rs != null) {
                rs.close();
            }
        }
        return result;
    }


    /**
     * 列印監考配當暨領卷簽名表
     * @param ht 條件值
     * @return 回傳 Vector 物件，內容為 Hashtable 的集合，<br>
     *         每一個 Hashtable 其 KEY 為欄位名稱，KEY 的值為欄位的值<br>
     *         若該欄位有中文名稱，則其 KEY 請加上 _NAME, EX: SMS 其中文欄位請設為 SMS_NAME
     * @throws Exception
     */
    public Vector getExat021ForExamTime(Hashtable ht) throws Exception
    {
        Vector result = new Vector();
        StringBuffer    sql        =    new StringBuffer();
        sql.append
        (
            "SELECT A.SECTION , A.CRSNO , B.CRS_SEQ , C.CENTER_CODE , C.CLASS_CODE  , E.CLSSRM_NAME AS CLSSRM_CODE  ,C.CMPS_CODE  ,D.CRS_NAME , A.AYEAR ,A.SMS , C.EXAM_TYPE , A.RESIT_TYPE,G.NAME AS TRATNAME  "  +
            "FROM EXAT021 A , EXAT019 B , EXAT017 C  , COUT002 D ,PLAT003 E ,SYST002 F,TRAT001 G,EXAT046 H " +
            "WHERE B.AYEAR = C.AYEAR "+
            "AND   B.SMS = C.SMS "+
            "AND   B.CRSNO = C.CRSNO "+
            "AND   A.AYEAR = C.AYEAR "+
            "AND   A.SMS = C.SMS "+
            "AND   A.CRSNO = C.CRSNO "+
            "AND   D.CRSNO = C.CRSNO "+
            "AND   F.CENTER_CODE = C.CENTER_CODE "+
            "AND   E.AYEAR = C.AYEAR "+
            "AND   E.SMS = C.SMS "+
            "AND   E.CENTER_ABRCODE = F.CENTER_ABRCODE "+
            "AND   E.CLSSRM_CODE = C.CLSSRM_CODE "+
            "AND   H.AYEAR(+)= C.AYEAR "+
            "AND   H.SMS(+)= C.SMS "+
            "AND   H.EXAM_TYPE(+)= C.EXAM_TYPE "+
            "AND   H.CENTER_CODE(+)= C.CENTER_CODE "+
            "AND   H.CLSSRM_CODE(+)= C.CLSSRM_CODE "+
            "AND   (H.RESIT_TYPE = A.RESIT_TYPE OR H.RESIT_TYPE IS NULL) "+
            "AND   (H.SECTION = A.SECTION OR H.SECTION IS NULL) "+
            "AND   G.IDNO(+)= H.IDNO "
        );

        if(!Utility.nullToSpace(ht.get("AYEAR")).equals(""))
            sql.append("AND C.AYEAR    =    '" + Utility.nullToSpace(ht.get("AYEAR"))+ "'");
        if(!Utility.nullToSpace(ht.get("SMS")).equals(""))
            sql.append("AND C.SMS    =    '" + Utility.nullToSpace(ht.get("SMS"))+ "'");
        if(!Utility.nullToSpace(ht.get("EXAM_TYPE")).equals(""))
            sql.append("AND C.EXAM_TYPE    =    '" + Utility.nullToSpace(ht.get("EXAM_TYPE"))+ "'");
        if(!Utility.nullToSpace(ht.get("CENTER_CODE")).equals(""))
            sql.append("AND C.CENTER_CODE    =    '" + Utility.nullToSpace(ht.get("CENTER_CODE"))+ "'");
        if(!Utility.nullToSpace(ht.get("CRSNO")).equals(""))
            sql.append("AND C.CRSNO    =    '" + Utility.nullToSpace(ht.get("CRSNO"))+ "'");
        if(!Utility.nullToSpace(ht.get("RESIT_TYPE")).equals(""))
            sql.append("AND A.RESIT_TYPE    =    '" + Utility.nullToSpace(ht.get("RESIT_TYPE"))+ "'");
        
        
        sql.append(" ORDER BY C.CENTER_CODE , A.SECTION ,B.CRS_SEQ " );
        System.out.println("exa412r="+sql.toString());
        DBResult rs = null;
        try{
            if(pageQuery) {
                // 依分頁取出資料
                rs = Page.getPageResultSet(dbmanager, conn, sql.toString(), pageNo, pageSize);
            } else {
                // 取出所有資料
                rs = dbmanager.getSimpleResultSet(conn);
                rs.open();
                rs.executeQuery(sql.toString());
            }
            Hashtable rowHt = null;
            while (rs.next()) {
                rowHt = new Hashtable();
                /** 將欄位抄一份過去 */
                for (int i = 1; i <= rs.getColumnCount(); i++){
                	String columpTemp = rs.getString(i);
                	if(columpTemp==null || columpTemp.trim().length()==0)
                		columpTemp = "&nbsp;";
                    rowHt.put(rs.getColumnName(i), columpTemp);
                }

                result.add(rowHt);
            }

        } catch (Exception e) {
            throw e;
        } finally {
            if(rs != null) {
                rs.close();
            }
        }
        return result;
    }
    
    /**
     * 列印監考配當暨領卷簽名表
     * @param ht 條件值
     * @return 回傳 Vector 物件，內容為 Hashtable 的集合，<br>
     *         每一個 Hashtable 其 KEY 為欄位名稱，KEY 的值為欄位的值<br>
     *         若該欄位有中文名稱，則其 KEY 請加上 _NAME, EX: SMS 其中文欄位請設為 SMS_NAME
     * @throws Exception
     */
    public Vector getExat021ForExamTime2(Hashtable ht) throws Exception
    {
        Vector result = new Vector();
        
        String resitype = ht.get("RESIT_TYPE").toString(); // 正補考類別
        
        StringBuffer    sql        =    new StringBuffer();
        
        // 正考的考試教室是經由編班,因此EXAT017和EXAT046可以對應的到資料
        if(resitype.equals("1")){
	        sql.append
	        (
	        	"SELECT C.CENTER_CODE, G.CENTER_NAME, C.CMPS_CODE, A.SECTION, B.CRS_SEQ||'-'||D.CRS_NAME||'('||C.CLASS_CODE||')' as COMBINE_DATA, " +
	        		"E.IDNO, F.NAME, C.CRSNO, D.CRS_NAME, C.CLASS_CODE, E.CLSSRM_CODE, H.CMPS_NAME , I.CLSSRM_NAME, " +
	        		"C.CLSSRM_CODE, G.CENTER_ABRCODE, B.CRS_SEQ, E.CMPS_CODE||E.SECTION||E.CLSSRM_CODE as CHECK_TEMP, "+
	        		"E.CMPS_CODE||E.SECTION||E.CLSSRM_CODE||C.CRSNO as CHECK_NAME_TEMP "+
	        	"FROM EXAT021 A, EXAT019 B, EXAT017 C, COUT002 D, EXAT046 E, TRAT001 F, SYST002 G, PLAT002 H, PLAT003 I "+
	        	"WHERE "+
	        	    "A.AYEAR='"+ht.get("AYEAR").toString()+"' "+
	        	    "AND A.SMS='"+ht.get("SMS").toString()+"' "+
	        	    "AND A.RESIT_TYPE='"+ht.get("RESIT_TYPE").toString()+"' "+
	        	    "AND C.EXAM_TYPE='"+ht.get("EXAM_TYPE").toString()+"' "+
	        	    "AND C.CENTER_CODE='"+ht.get("CENTER_CODE").toString()+"' "+
	        	    "AND C.CMPS_CODE like '%"+ht.get("CMPS_CODE").toString()+"' "+
	        	    "AND B.AYEAR= A.AYEAR "+
	        	    "AND B.SMS=A.SMS "+
	        	    "AND B.CRSNO=A.CRSNO "+
	        	    "AND C.AYEAR= A.AYEAR "+
	        	    "AND C.SMS=A.SMS  "+
	        	    "AND C.CRSNO=A.CRSNO "+
	        	    "AND D.CRSNO=A.CRSNO "+
	        	    "AND E.AYEAR= A.AYEAR  "+
	        	    "AND E.SMS=A.SMS  "+
	        	    "AND E.EXAM_TYPE=C.EXAM_TYPE  "+
	        	    "AND E.RESIT_TYPE=A.RESIT_TYPE  "+
	        	    "AND E.CENTER_CODE=C.CENTER_CODE  "+
	        	    "AND E.CMPS_CODE=C.CMPS_CODE  "+
	        	    "AND E.SECTION=A.SECTION  "+
	        	    "AND E.CLSSRM_CODE=C.CLSSRM_CODE "+
	        	    "AND F.IDNO=E.IDNO "+
	        	    "AND G.CENTER_CODE= C.CENTER_CODE  "+
	        	    "AND H.AYEAR=C.AYEAR  "+
	        	    "AND H.SMS=C.SMS  "+
	        	    "AND H.CENTER_ABRCODE=G.CENTER_ABRCODE  "+
	        	    "AND H.CMPS_CODE=C.CMPS_CODE "+
	        	    "AND I.AYEAR= E.AYEAR  "+
	        	    "AND I.SMS=E.SMS  "+
	        	    "AND I.CENTER_ABRCODE=G.CENTER_ABRCODE  "+
	        	    "AND I.CMPS_CODE=E.CMPS_CODE  "+
	        	    "AND I.CLSSRM_CODE=E.CLSSRM_CODE "+
	        	"ORDER BY C.CENTER_CODE, C.CMPS_CODE, A.SECTION, C.CLSSRM_CODE, E.IDNO, B.CRS_SEQ "
	        );
        }
        // 補考教室是中心自己編
        else if(resitype.equals("2")){
        	sql.append(
		        	"SELECT DISTINCT C.CENTER_CODE, G.CENTER_NAME, C.CMPS_CODE, A.SECTION, B.CRS_SEQ||'-'||D.CRS_NAME||'('||C.CLASS_CODE||')' as COMBINE_DATA, " +
		    		"E.IDNO, F.NAME, C.CRSNO, D.CRS_NAME, C.CLASS_CODE, E.CLSSRM_CODE, H.CMPS_NAME , I.CLSSRM_NAME, " +
		    		"C.CLSSRM_CODE, G.CENTER_ABRCODE, B.CRS_SEQ, E.CMPS_CODE||E.SECTION||E.CLSSRM_CODE as CHECK_TEMP, "+
		    		"E.CMPS_CODE||E.SECTION||E.CLSSRM_CODE||C.CRSNO as CHECK_NAME_TEMP "+
		    	"FROM EXAT021 A, EXAT019 B, EXAT048 C, COUT002 D, EXAT046 E, TRAT001 F, SYST002 G, PLAT002 H, PLAT003 I "+
		    	"WHERE "+
		    	    "    A.AYEAR='"+ht.get("AYEAR").toString()+"' "+
		    	    "AND A.SMS='"+ht.get("SMS").toString()+"' "+
		    	    "AND A.RESIT_TYPE='"+ht.get("RESIT_TYPE").toString()+"' "+
		    	    "AND C.EXAM_TYPE='"+ht.get("EXAM_TYPE").toString()+"' "+
		    	    "AND C.CENTER_CODE='"+ht.get("CENTER_CODE").toString()+"' "+
		    	    "AND C.CMPS_CODE like '%"+ht.get("CMPS_CODE").toString()+"' "+
		    	    "AND B.AYEAR= A.AYEAR "+
		    	    "AND B.SMS=A.SMS "+
		    	    "AND B.CRSNO=A.CRSNO "+
		    	    "AND C.AYEAR= A.AYEAR "+
		    	    "AND C.SMS=A.SMS "+
		    	    "AND C.CRSNO=A.CRSNO "+
		    	    "AND C.SECTION=A.SECTION "+
		    	    "AND D.CRSNO=A.CRSNO "+	
		    	    "AND E.AYEAR= C.AYEAR "+
		    	    "AND E.SMS=C.SMS  "+
		    	    "AND E.EXAM_TYPE=C.EXAM_TYPE  "+
		    	    "AND E.CENTER_CODE=C.CENTER_CODE  "+
		    	    "AND E.CMPS_CODE=C.CMPS_CODE  "+
		    	    "AND E.SECTION=C.SECTION "+
		    	    "AND E.CLSSRM_CODE=C.CLSSRM_CODE "+
		    	    "AND E.RESIT_TYPE=A.RESIT_TYPE "+
		    	    "AND F.IDNO=E.IDNO "+
		    	    "AND G.CENTER_CODE= C.CENTER_CODE  "+
		    	    "AND H.AYEAR=C.AYEAR  "+
		    	    "AND H.SMS=C.SMS  "+
		    	    "AND H.CENTER_ABRCODE=G.CENTER_ABRCODE  "+
		    	    "AND H.CMPS_CODE=C.CMPS_CODE "+
		    	    "AND I.AYEAR= E.AYEAR  "+
		    	    "AND I.SMS=E.SMS  "+
		    	    "AND I.CENTER_ABRCODE=G.CENTER_ABRCODE  "+
		    	    "AND I.CMPS_CODE=E.CMPS_CODE  "+
		    	    "AND I.CLSSRM_CODE=E.CLSSRM_CODE "+
		    	"ORDER BY C.CENTER_CODE, C.CMPS_CODE, A.SECTION, C.CLSSRM_CODE, E.IDNO, B.CRS_SEQ "
        	);
        }

        System.out.println("exa412r="+sql.toString());
        DBResult rs = null;
        try{
            if(pageQuery) {
                // 依分頁取出資料
                rs = Page.getPageResultSet(dbmanager, conn, sql.toString(), pageNo, pageSize);
            } else {
                // 取出所有資料
                rs = dbmanager.getSimpleResultSet(conn);
                rs.open();
                rs.executeQuery(sql.toString());
            }
            
            Vector temp = new Vector();
            Hashtable rowHt = null;
            while (rs.next()) {
                rowHt = new Hashtable();
                /** 將欄位抄一份過去 */
                for (int i = 1; i <= rs.getColumnCount(); i++)
                    rowHt.put(rs.getColumnName(i), rs.getString(i));

                temp.add(rowHt);
            }
            
            // 將同節課同間教室的科目班級班級序號組好
            String nextCheck = ""; // 下一筆資料的比較變數
            String totalCombineData = ""; // 所欲組合的資料
            Set nameSet = new HashSet();
            for(int i=0; i<temp.size(); i++){
            	Hashtable content = (Hashtable)temp.get(i);
            	String check = content.get("CHECK_TEMP").toString();
            	String name = content.get("NAME").toString();
            	
            	// 檢核考試教室是否改變
            	if(i!=temp.size()-1)
            		nextCheck = ((Hashtable)temp.get(i+1)).get("CHECK_TEMP").toString();
            	else
            		nextCheck = "";
            	
            	if(!totalCombineData.equals(""))
        			totalCombineData+="<br>";
            	totalCombineData+=content.get("COMBINE_DATA").toString();
            	
            	// 存放每筆的姓名(用set是因為姓名相同不會在加上去)
            	nameSet.add(name);
            	            	
            	// 如和下筆資料相同則累計考試科目相關資料
            	if(!check.equals(nextCheck)){
            		String totalName = "";
            		// 取得同節課中的所有監試人員姓名
            		Iterator it = nameSet.iterator();
            		while(it.hasNext()){
            			if(!totalName.equals(""))
            				totalName+="<br>";
            			totalName+=it.next().toString();
            		}
            		
            		Hashtable tempHt = content;
            		tempHt.put("COMBINE_DATA", totalCombineData);
            		tempHt.put("NAME", totalName);
            		result.add(tempHt);
            		
            		// 清空檢核變數
            		totalCombineData="";
            		nameSet = new HashSet();
            	}
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if(rs != null) {
                rs.close();
            }
        }
        return result;
    }


    /**
     * 列印監試人員監考節次統計表 - 取得節次時間資料 (EXAT021, EXAT041)
     * @param ht 條件值
     * @return 回傳 Vector 物件，內容為 Hashtable 的集合，<br>
     *         每一個 Hashtable 其 KEY 為欄位名稱，KEY 的值為欄位的值<br>
     *         若該欄位有中文名稱，則其 KEY 請加上 _NAME, EX: SMS 其中文欄位請設為 SMS_NAME
     * @throws Exception
     */
    public Vector getSectionAndTime(Hashtable ht) throws Exception
    {
        Vector      result  =   null;
        DBResult    rs      =   null;


        try
        {
            result  =   new Vector();


            //將 SQL 清空
            if (sql.length() > 0)
                sql.delete(0, sql.length());


            sql.append
            (
                "SELECT " +
                "EXAT021.SECTION, EXAT041.STIME, EXAT041.ETIME " +
                "FROM EXAT021, EXAT041 " +
                "WHERE " +
                "1 = 1 "
            );


            //JOIN 條件
            sql.append
            (
                "AND EXAT021.RESIT_TYPE = EXAT041.RESIT_TYPE " +
                "AND SUBSTR(EXAT021.SECTION,2,1) = EXAT041.SECTION "
            );


            //條件欄位
            String  AYEAR           =   Utility.checkNull(ht.get("AYEAR"), "");             //學年
            String  SMS             =   Utility.checkNull(ht.get("SMS"), "");               //學期
            String  RESIT_TYPE      =   Utility.checkNull(ht.get("RESIT_TYPE"), "");        //正補考別


            //加入條件 - 學年
            if (!"".equals(AYEAR))
                sql.append("AND EXAT021.AYEAR = '" + AYEAR + "' ");

            //加入條件 - 學期
            if (!"".equals(SMS))
                sql.append("AND EXAT021.SMS = '" + SMS + "' ");

            //加入條件 - 正補考別
            if (!"".equals(RESIT_TYPE))
                sql.append("AND EXAT021.RESIT_TYPE = '" + RESIT_TYPE + "' ");


            //GROUP
            sql.append("GROUP BY EXAT021.SECTION, EXAT041.STIME, EXAT041.ETIME ");


            //排序
            if (!"".equals(orderBy))
            {
                sql.append("ORDER BY " + orderBy);
                orderBy = "";
            }


            System.out.println(sql.toString());


            // 依分頁取出資料
            if (pageQuery)
            {
                rs = Page.getPageResultSet(dbmanager, conn, sql.toString(), pageNo, pageSize);
            }
            // 取出所有資料
            else
            {
                rs = dbmanager.getSimpleResultSet(conn);
                rs.open();
                rs.executeQuery(sql.toString());
            }


            Hashtable   rowHt   =   null;

            while (rs.next())
            {
                rowHt   =   new Hashtable();

                /** 將欄位抄一份過去 */
                for (int i = 1; i <= rs.getColumnCount(); i++)
                    rowHt.put(rs.getColumnName(i), rs.getString(i));

                result.add(rowHt);
            }


            return result;
        }
        catch(Exception e)
        {
            throw e;
        }
        finally
        {
            if (rs != null)
                rs.close();
        }
    }

    /**
     * 列印監考配當暨領卷簽名表 - 取得節次時間資料 (EXAT021 E21 , EXAT017 E17 , COUT002 C02  )
     * @param ht 條件值
     * @return 回傳 Vector 物件，內容為 Hashtable 的集合，<br>
     *         每一個 Hashtable 其 KEY 為欄位名稱，KEY 的值為欄位的值<br>
     *         若該欄位有中文名稱，則其 KEY 請加上 _NAME, EX: SMS 其中文欄位請設為 SMS_NAME
     * @throws Exception
     */
    public Vector getExat021ForExamDate(Hashtable ht) throws Exception
    {
        Vector result = new Vector();
        StringBuffer    sql        =    new StringBuffer();
              
        // 判斷是否併班標準--->同一節課中該教室是否有超過一個以上的班級在考試
        sql.append(
        	"SELECT a.crsno, a.crs_name, a.master_class_code, " +
        		"DECODE(b.THIS_SECTION_CLSSRM_COUNT,'1','','(併班)') AS EXAM_CLSSRM_MK "+
        	"FROM "+
        		"( "+
	        		"SELECT E17.CRSNO,C02.CRS_NAME,E17.CLASS_CODE AS MASTER_CLASS_CODE,E21.SECTION,E21.RESIT_TYPE, "+
	        			"E17.EXAM_TYPE,E17.CLSSRM_CODE,E17.CMPS_CODE,E17.CENTER_CODE,E17.AYEAR, E17.SMS "+
	        		"FROM EXAT021 E21 , EXAT017 E17 , EXAT046 E46 , COUT002 C02 "+
	        		"WHERE 1=1 "+
		        		"AND E46.AYEAR = E17.AYEAR  "+
		        		"AND E46.SMS = E17.SMS  "+
		        		"AND E46.EXAM_TYPE = E17.EXAM_TYPE  "+
		        		"AND E46.CENTER_CODE = E17.CENTER_CODE  "+
		        		"AND E46.CMPS_CODE = E17.CMPS_CODE  "+
		        		"AND E46.SECTION = E21.SECTION  "+
		        		"AND E46.CLSSRM_CODE = E17.CLSSRM_CODE  "+
		        		"AND E46.RESIT_TYPE = '1'  "+
		        		"AND E46.IDNO like '%"+Utility.nullToSpace(ht.get("IDNO"))+"%'  "+       		
	        			"AND E21.AYEAR = E17.AYEAR  "+
	        			"AND E21.SMS = E17.SMS  "+
	        			"AND E21.CRSNO = E17.CRSNO  "+
	        			"AND E17.CRSNO = C02.CRSNO  "+
	        			"AND E17.CMPS_CODE like '%"+Utility.nullToSpace(ht.get("CMPS_CODE"))+"'  "+
	        			"AND E17.CLSSRM_CODE like '%"+Utility.nullToSpace(ht.get("CLSSRM_CODE"))+"' "+ 
	        			"AND E21.SECTION   like '%"+Utility.nullToSpace(ht.get("SECTION"))+"'  "+
	        			"AND E21.AYEAR   like '%"+Utility.nullToSpace(ht.get("AYEAR"))+"' "+ 
	        			"AND E21.SMS   like '%"+Utility.nullToSpace(ht.get("SMS"))+"'  "+
	        			"AND E21.RESIT_TYPE    =    '1'  "+
	        			"AND E17.EXAM_TYPE   like '%"+Utility.nullToSpace(ht.get("EXAM_TYPE"))+"' "+
	        			"AND E17.CENTER_CODE   like '%"+Utility.nullToSpace(ht.get("CENTER_CODE"))+"'   "+
	        		"GROUP BY E17.AYEAR,E17.SMS,E17.CENTER_CODE,E17.CMPS_CODE,E17.CLSSRM_CODE,E17.EXAM_TYPE,E21.SECTION,E21.RESIT_TYPE,E17.CRSNO, C02.CRS_NAME, E17.CLASS_CODE "+
        		") a, "+
        		"(" +
        			"SELECT COUNT(1) AS THIS_SECTION_CLSSRM_COUNT ,a.ayear,a.sms,a.exam_type,b.resit_type," +
        				"a.center_code,a.cmps_code,b.section, a.clssrm_code "+
        			"FROM EXAT017 a, EXAT021 b , EXAT046 E46 "+
        			"WHERE 1=1 "+
	        			"AND E46.AYEAR = a.AYEAR  "+
		        		"AND E46.SMS = a.SMS  "+
		        		"AND E46.EXAM_TYPE = a.EXAM_TYPE  "+
		        		"AND E46.CENTER_CODE = a.CENTER_CODE  "+
		        		"AND E46.CMPS_CODE = a.CMPS_CODE  "+
		        		"AND E46.SECTION = b.SECTION  "+
		        		"AND E46.CLSSRM_CODE = a.CLSSRM_CODE  "+
		        		"AND E46.RESIT_TYPE = '1'  "+
		        		"AND E46.IDNO like '%"+Utility.nullToSpace(ht.get("IDNO"))+"%'  "+ 
        				"AND b.AYEAR = a.AYEAR  "+
        				"AND b.SMS = a.SMS  "+
        				"AND b.CRSNO = a.CRSNO  "+
        				"AND b.RESIT_TYPE    =    '1'  "+
        				"AND a.CMPS_CODE like '%"+Utility.nullToSpace(ht.get("CMPS_CODE"))+"'  "+
	        			"AND a.CLSSRM_CODE like '%"+Utility.nullToSpace(ht.get("CLSSRM_CODE"))+"' "+ 
	        			"AND b.SECTION   like '%"+Utility.nullToSpace(ht.get("SECTION"))+"'  "+
	        			"AND b.AYEAR   like '%"+Utility.nullToSpace(ht.get("AYEAR"))+"' "+ 
	        			"AND b.SMS   like '%"+Utility.nullToSpace(ht.get("SMS"))+"'  "+
	        			"AND b.RESIT_TYPE    =    '1'  "+
	        			"AND a.EXAM_TYPE   like '%"+Utility.nullToSpace(ht.get("EXAM_TYPE"))+"' "+
	        			"AND a.CENTER_CODE   like '%"+Utility.nullToSpace(ht.get("CENTER_CODE"))+"'   "+
        			"GROUP BY a.ayear,a.sms,a.exam_type,b.resit_type,a.center_code,a.cmps_code,b.section, a.clssrm_code "+
        		") b "+
        		"WHERE "+
        			"    b.ayear=a.ayear "+
        			"and b.sms=a.sms "+
        			"and b.center_code=a.center_code "+
        			"and b.cmps_code=a.cmps_code "+
        			"and b.clssrm_code=a.clssrm_code "+
        			"and b.exam_type=a.exam_type "+
        			"and b.resit_type=a.resit_type "+
        			"and b.section=a.section "
        );
        
        DBResult rs = null;
        try{
            if(pageQuery) {
                // 依分頁取出資料
                rs = Page.getPageResultSet(dbmanager, conn, sql.toString(), pageNo, pageSize);
            } else {
                // 取出所有資料
                rs = dbmanager.getSimpleResultSet(conn);
                rs.open();
                rs.executeQuery(sql.toString());
            }
            Hashtable rowHt = null;
            while (rs.next()) {
               rowHt = new Hashtable();
               /** 將欄位抄一份過去 */
               for (int i = 1; i <= rs.getColumnCount(); i++) {
                   rowHt.put(rs.getColumnName(i), rs.getString(i));
               }
               result.add(rowHt);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if(rs != null) {
                rs.close();
            }
        }
        return result;
    }
    
    /**
     * 列印寄考學生通知單 - 取得節次時間資料 (EXAT021 E21 , EXAT017 E17 , COUT002 C02  )
     * @param ht 條件值
     * @return 回傳 Vector 物件，內容為 Hashtable 的集合，<br>
     *         每一個 Hashtable 其 KEY 為欄位名稱，KEY 的值為欄位的值<br>
     *         若該欄位有中文名稱，則其 KEY 請加上 _NAME, EX: SMS 其中文欄位請設為 SMS_NAME
     * @throws Exception
     */
    public Vector getExat429ForExamDate(Hashtable ht) throws Exception
    {
        Vector result = new Vector();
        StringBuffer    sql        =    new StringBuffer();
              
        // 判斷是否併班標準--->同一節課中該教室是否有超過一個以上的班級在考試
        sql.append(
        	"SELECT a.crsno, a.crs_name, a.master_class_code, a.class_code " +
        	"FROM "+
        		"( "+
	        		"SELECT E26.CRSNO,C02.CRS_NAME,E17.CLASS_CODE AS MASTER_CLASS_CODE,E21.SECTION,E21.RESIT_TYPE, "+
	        		"E26.EXAM_TYPE,E26.CLSSRM_CODE,E26.CMPS_CODE,E26.CENTER_CODE,E26.AYEAR, E26.SMS, E26.CLASS_CODE "+
	        		"FROM EXAT026 E26 "+
	        		"left JOIN EXAT017 E17 ON E26.AYEAR = E17.AYEAR AND E26.SMS = E17.SMS AND E26.EXAM_TYPE = E17.EXAM_TYPE AND E26.CENTER_CODE  = E17.CENTER_CODE AND E26.CMPS_CODE = E17.CMPS_CODE AND E26.CRSNO = E17.CRSNO AND E26.CLSSRM_CODE = E17.CLSSRM_CODE "+
	        		"JOIN EXAT021 E21 ON E21.AYEAR = E26.AYEAR AND E21.SMS = E26.SMS AND E21.RESIT_TYPE = '1' AND E21.CRSNO = E26.CRSNO "+
	        		"JOIN COUT002 C02 ON C02.CRSNO =E26.CRSNO "+
	        		"WHERE 1=1 "+
		        		"AND E21.RESIT_TYPE = '1'  "+
		        		"AND E26.STNO like '%"+Utility.nullToSpace(ht.get("STNO"))+"%'  "+
	        			"AND E26.CMPS_CODE like '%"+Utility.nullToSpace(ht.get("CMPS_CODE"))+"'  "+
	        			"AND E26.CLSSRM_CODE like '%"+Utility.nullToSpace(ht.get("CLSSRM_CODE"))+"' "+ 
	        			"AND E21.SECTION   like '%"+Utility.nullToSpace(ht.get("SECTION"))+"'  "+
	        			"AND E26.AYEAR   like '%"+Utility.nullToSpace(ht.get("AYEAR"))+"' "+ 
	        			"AND E26.SMS   like '%"+Utility.nullToSpace(ht.get("SMS"))+"'  "+
	        			"AND E26.EXAM_TYPE   like '%"+Utility.nullToSpace(ht.get("EXAM_TYPE"))+"' "+
	        			"AND E26.CENTER_CODE   like '%"+Utility.nullToSpace(ht.get("CENTER_CODE"))+"'   "+
	        		//"GROUP BY E26.AYEAR,E26.SMS,E26.CENTER_CODE,E26.CMPS_CODE,E26.CLSSRM_CODE,E26.EXAM_TYPE,E21.SECTION,E17.CRSNO, C02.CRS_NAME, E17.CLASS_CODE "+
        		") a "+
        		"WHERE 1=1 "+
        		"AND ROWNUM = 1 "+
        		"ORDER BY a.crsno, a.crs_name, a.master_class_code, a.class_code "
        );
        
        DBResult rs = null;
        try{
            if(pageQuery) {
                // 依分頁取出資料
                rs = Page.getPageResultSet(dbmanager, conn, sql.toString(), pageNo, pageSize);
            } else {
                // 取出所有資料
                rs = dbmanager.getSimpleResultSet(conn);
                rs.open();
                rs.executeQuery(sql.toString());
            }
            Hashtable rowHt = null;
            while (rs.next()) {
               rowHt = new Hashtable();
               /** 將欄位抄一份過去 */
               for (int i = 1; i <= rs.getColumnCount(); i++) {
                   rowHt.put(rs.getColumnName(i), rs.getString(i));
               }
               result.add(rowHt);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if(rs != null) {
                rs.close();
            }
        }
        return result;
    }

    /**
     * EXA013M_設定科目考試節次 查詢
     * @param ht 條件值
     * @return 回傳 Vector 物件，內容為 Hashtable 的集合，<br>
     *         每一個 Hashtable 其 KEY 為欄位名稱，KEY 的值為欄位的值<br>
     *         若該欄位有中文名稱，則其 KEY 請加上 _NAME, EX: SMS 其中文欄位請設為 SMS_NAME
     * @throws Exception
     */
    public Vector getEXA013M(Hashtable ht) throws Exception
    {
        Vector result = new Vector();
        if(sql.length() > 0) {
            sql.delete(0, sql.length());
        }
		sql.append(
			"SELECT E21.AYEAR, E21.SMS, E21.SECTION, E41.STIME, E41.ETIME, E21.CRSNO, E19.CRS_SEQ, C02.CRS_NAME, E21.RESIT_TYPE, " +
            "SUBSTR(E21.SECTION, 1, 1) AS DATE_WEEK, SUBSTR(E21.SECTION, 2, 1) AS SECTION_TIME,E21.LOCK_MK " +
            "FROM EXAT021 E21, EXAT019 E19, COUT002 C02, EXAT041 E41 " +
            "WHERE E19.AYEAR (+)= E21.AYEAR " +
            "AND E19.SMS (+)= E21.SMS " +
	        "AND E19.CRSNO (+)= E21.CRSNO " +
	        "AND C02.CRSNO (+)= E21.CRSNO " +
            "AND E41.SECTION (+)= SUBSTR(E21.SECTION, 2, 1) " +
            "AND E41.RESIT_TYPE = '" + Utility.nullToSpace(ht.get("RESIT_TYPE")) + "' "
        );
        if(!Utility.nullToSpace(ht.get("AYEAR")).equals("")) {
            sql.append("AND E21.AYEAR = '" + Utility.nullToSpace(ht.get("AYEAR")) + "' ");
        }
        if(!Utility.nullToSpace(ht.get("SMS")).equals("")) {
            sql.append("AND E21.SMS = '" + Utility.nullToSpace(ht.get("SMS")) + "' ");
        }
        if(!Utility.nullToSpace(ht.get("SECTION")).equals("")) {
            sql.append("AND E21.SECTION LIKE '" + Utility.nullToSpace(ht.get("SECTION")) + "%' ");
        }
        if(!Utility.nullToSpace(ht.get("RESIT_TYPE")).equals("")) {
            sql.append("AND E21.RESIT_TYPE = '" + Utility.nullToSpace(ht.get("RESIT_TYPE")) + "' ");
        }

        sql.append("ORDER BY E19.CRS_SEQ, E21.CRSNO");

        DBResult rs = null;
        try {
            if(pageQuery) {
                // 依分頁取出資料
                rs = Page.getPageResultSet(dbmanager, conn, sql.toString(), pageNo, pageSize);
            } else {
                // 取出所有資料
                rs = dbmanager.getSimpleResultSet(conn);
                rs.open();
                rs.executeQuery(sql.toString());
            }
            Hashtable rowHt = null;
            while (rs.next()) {
                rowHt = new Hashtable();
                /** 將欄位抄一份過去 */
                for (int i = 1; i <= rs.getColumnCount(); i++)
                    rowHt.put(rs.getColumnName(i), rs.getString(i));

                result.add(rowHt);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if(rs != null) {
                rs.close();
            }
        }
        return result;
    }

     /**
     * 列印考試監試總表 - 取得表頭考試時段資料 (EXAT021 ,EXAT041 )
     * @param ht 條件值
     * @return 回傳 Vector 物件，內容為 Hashtable 的集合，<br>
     *         每一個 Hashtable 其 KEY 為欄位名稱，KEY 的值為欄位的值<br>
     *         若該欄位有中文名稱，則其 KEY 請加上 _NAME, EX: SMS 其中文欄位請設為 SMS_NAME
     * @throws Exception
     */
    public Vector getExat417RForExamDate(Hashtable ht) throws Exception
    {
         Vector result = new Vector();
         DBResult rs = null;
         try{
            sql.append( " SELECT EXAT021.SECTION,EXAT041.STIME,EXAT041.ETIME " +
                        " FROM EXAT021,EXAT041 " +
                        " WHERE EXAT021.AYEAR = '"+ht.get("AYEAR")+"' " +
                        " AND EXAT041.SECTION = SUBSTR(EXAT021.SECTION,2,1) " +
                        " AND EXAT021.RESIT_TYPE = '"+ht.get("RESIT_TYPE")+"' " +
                        " AND EXAT021.SMS = '"+ht.get("SMS")+"' " +
                        " AND EXAT021.RESIT_TYPE = EXAT041.RESIT_TYPE " +
                        " GROUP BY EXAT021.SECTION,EXAT041.STIME,EXAT041.ETIME "+
                        " ORDER BY EXAT021.SECTION "
            );


                // 取出所有資料
                rs = dbmanager.getSimpleResultSet(conn);
                rs.open();
                rs.executeQuery(sql.toString());
System.out.println("exa417r="+sql.toString());
            Hashtable rowHt = null;

            while (rs.next()) {
                rowHt = new Hashtable();

                /** 將欄位抄一份過去 */
                for (int i = 1; i <= rs.getColumnCount(); i++) {
                    rowHt.put(rs.getColumnName(i), rs.getString(i));
                }
                result.add(rowHt);
            }
        }
        catch (Exception e) {
            throw e;
        } finally {
            if(rs != null) {
                rs.close();
            }
        }
        return result;
    }


    /**
     * 列印考試座次表 (EXAT022,EXAT002,EXAT020, SYST001,SYST002)
     * @param ht 條件值
     * @return 回傳 Vector 物件，內容為 Hashtable 的集合，<br>
     *         每一個 Hashtable 其 KEY 為欄位名稱，KEY 的值為欄位的值<br>
     *         若該欄位有中文名稱，則其 KEY 請加上 _NAME, EX: SMS 其中文欄位請設為 SMS_NAME
     *         returnClassmStuNum:每間教室中該科的考試人數
     * @throws Exception
     */
    public Vector getExaNumSeat(Vector vt,Vector vt2,Vector vt3,Vector vt4, Hashtable ht, Hashtable returnClassmStuNum) throws Exception
    {
        Vector      result  =   null;
        DBResult    rs      =   null;
		DBResult    rs1      =   null;

        try
        {
            result  =   new Vector();
            
            String  AYEAR           =   Utility.checkNull(ht.get("AYEAR"), "");				//學年
            String  SMS             =   Utility.checkNull(ht.get("SMS"), "");					//學期
            String  EXAM_TYPE       =   Utility.checkNull(ht.get("EXAM_TYPE"), "");			//考試類別
			String  CENTER_CODE     =   Utility.checkNull(ht.get("CENTER_CODE"), "");			//中心別
			String  SECTION     =   Utility.checkNull(ht.get("SECTION"), "");					//節次
			String  CMPS_CODE     =   Utility.checkNull(ht.get("CMPS_CODE"), "");				//校區代號
			String CLSSRM_CODE	=   Utility.checkNull(ht.get("CLSSRM_CODE"), "");				//考試教室

			// vtData1考試教室主要基本資料---------------------------------------------------------------開始
            //將 SQL 清空
            if (sql.length() > 0)
                sql.delete(0, sql.length());
            
            sql.append
            (
                " SELECT DISTINCT '1',a.SECTION, b.CENTER_CODE, b.CMPS_CODE, b.CLSSRM_CODE, c.CRSNO, c.CRS_NAME , " +
				" d.CRS_SEQ,e.CENTER_ABRCODE, F.CMPS_NAME, E.CENTER_NAME, G.CLSSRM_NAME, b.CLASS_CODE, (select to_char(to_date(stime, 'hh24mi'), 'hh24:mi') || '-' || to_char(to_date(etime, 'hh24mi'), 'hh24:mi') from exat041 where resit_type = '1' and section = substr(a.section, 2, 1)) as exam_time " +
                " FROM EXAT021 a, EXAT017 b, COUT002 c, EXAT019 d,syst002 e, PLAT002 F, PLAT003 G " +
                " WHERE  "+
                " b.AYEAR  =  a.AYEAR "+
                " AND b.SMS =  a.SMS AND b.CRSNO=a.CRSNO "+
                " AND c.CRSNO = b.CRSNO "+
                " AND d.AYEAR = a.AYEAR "+
                " AND d.SMS = a.SMS "+
                " AND d.CRSNO= b.CRSNO "+
                " AND a.RESIT_TYPE = '1' "+
                " AND b.CENTER_CODE = e.CENTER_CODE "+
                " AND B.AYEAR=F.AYEAR "+
                " AND B.SMS=F.SMS "+
                " AND B.CMPS_CODE=F.CMPS_CODE "+
                " AND F.CENTER_ABRCODE=E.CENTER_ABRCODE "+
                " AND B.AYEAR=G.AYEAR "+
                " AND B.SMS=G.SMS "+
                " AND B.CMPS_CODE=G.CMPS_CODE "+
                " AND G.CENTER_ABRCODE=E.CENTER_ABRCODE "+
                " AND B.CLSSRM_CODE = g.CLSSRM_CODE AND b.CLSSRM_CODE != '000000' "
            );

	        if(!AYEAR.equals("")) 
	            sql.append(" AND a.AYEAR = '" + Utility.nullToSpace(ht.get("AYEAR")) + "' ");
	        if(!SMS.equals("")) 
	            sql.append(" AND a.SMS = '" + Utility.nullToSpace(ht.get("SMS")) + "' ");
	        if(!EXAM_TYPE.equals("")) 
	            sql.append(" AND b.EXAM_TYPE = '" + Utility.nullToSpace(ht.get("EXAM_TYPE")) + "' ");
	        if(!CENTER_CODE.equals("")) 
	            sql.append(" AND b.CENTER_CODE = '" + Utility.nullToSpace(ht.get("CENTER_CODE")) + "' ");
	        if(!SECTION.equals("")) 
	            sql.append(" AND a.SECTION = '" + Utility.nullToSpace(ht.get("SECTION")) + "' ");
	        if(!CMPS_CODE.equals(""))
	            sql.append(" AND b.CMPS_CODE = '" + Utility.nullToSpace(ht.get("CMPS_CODE")) + "' ");
	        if(!CLSSRM_CODE.equals(""))
	            sql.append(" AND b.CLSSRM_CODE = '" + Utility.nullToSpace(ht.get("CLSSRM_CODE")) + "' ");
	        
	        sql.append(" UNION ");

	        sql.append("SELECT DISTINCT '2',a.SECTION, A.CENTER_CODE, A.CMPS_CODE, A.CLSSRM_CODE, c.CRSNO, c.CRS_NAME ,  d.CRS_SEQ,e.CENTER_ABRCODE, F.CMPS_NAME, E.CENTER_NAME, G.CLSSRM_NAME, A.CLASS_CODE, "+
	                   "      (select to_char(to_date(stime, 'hh24mi'), 'hh24:mi') || '-' || to_char(to_date(etime, 'hh24mi'), 'hh24:mi') "+
	                   "       from exat041 "+
	                   "       where resit_type = '1' "+
	                   "       and section = substr(a.section, 2, 1)) as exam_time "+
	                   "FROM EXAT026 a, COUT002 c, EXAT019 d,syst002 e, PLAT002 F, PLAT003 G "+
	                   "WHERE C.CRSNO = A.CRSNO "+
	                   "AND   D.AYEAR = A.AYEAR "+
	                   "AND   D.SMS = A.SMS "+
	                   "AND   D.CRSNO = A.CRSNO "+
	                   "AND   E.CENTER_CODE = A.CENTER_CODE "+
	                   "AND   F.AYEAR = A.AYEAR "+
	                   "AND   F.SMS = A.SMS "+
	                   "AND   F.CENTER_ABRCODE = E.CENTER_ABRCODE "+
	                   "AND   F.CMPS_CODE = A.CMPS_CODE "+
	                   "AND   G.AYEAR = A.AYEAR "+
	                   "AND   G.SMS = A.SMS "+
	                   "AND   G.CENTER_ABRCODE = E.CENTER_ABRCODE "+
	                   "AND   G.CMPS_CODE = A.CMPS_CODE "+
	                   "AND   G.CLSSRM_CODE = A.CLSSRM_CODE AND A.CLSSRM_CODE != '000000' " );
            
	        if(!AYEAR.equals("")) 
	            sql.append(" AND a.AYEAR = '" + Utility.nullToSpace(ht.get("AYEAR")) + "' ");
	        if(!SMS.equals("")) 
	            sql.append(" AND a.SMS = '" + Utility.nullToSpace(ht.get("SMS")) + "' ");
	        if(!EXAM_TYPE.equals("")) 
	            sql.append(" AND A.EXAM_TYPE = '" + Utility.nullToSpace(ht.get("EXAM_TYPE")) + "' ");
	        
	        sql.append(" AND A.EXAM_APP_TYPE = '4' ");
	        
	        if(!CENTER_CODE.equals("")) 
	            sql.append(" AND A.CENTER_CODE = '" + Utility.nullToSpace(ht.get("CENTER_CODE")) + "' ");
	        if(!SECTION.equals("")) 
	            sql.append(" AND A.SECTION = '" + Utility.nullToSpace(ht.get("SECTION")) + "' ");
	        if(!CMPS_CODE.equals(""))
	            sql.append(" AND A.CMPS_CODE = '" + Utility.nullToSpace(ht.get("CMPS_CODE")) + "' ");
	        if(!CLSSRM_CODE.equals(""))
	            sql.append(" AND A.CLSSRM_CODE = '" + Utility.nullToSpace(ht.get("CLSSRM_CODE")) + "' ");


	       
	   //     sql.append(" ORDER BY 1,2,3,6,8,4 ");
	        sql.append(" ORDER BY  2, 3, 4, 5, 6, 1 ");
            
	        System.out.println("getExaNumSeat = " + sql.toString());

            rs = dbmanager.getSimpleResultSet(conn);
            rs.open();
            rs.executeQuery(sql.toString());


            Hashtable   rowHt   =   null;
            String totalCrsno = "''"; // 所查詢出來的所有科目
            while (rs.next())
            {
                rowHt   =   new Hashtable();

                // 將欄位抄一份過去
                for (int i = 1; i <= rs.getColumnCount(); i++)
                    rowHt.put(rs.getColumnName(i), rs.getString(i));

                vt.add(rowHt);
                totalCrsno+=",'"+rs.getString("CRSNO")+"'";
            }
            //System.out.println("size:"+vt.size());
            // vtData1考試教室主要基本資料---------------------------------------------------------------結束

            // vtData2考試日期時間資料---------------------------------------------------------------開始
			//3b.帶出考試日期時間
            Hashtable inputHt = new Hashtable();
            inputHt.put("AYEAR", AYEAR);
            inputHt.put("SMS", SMS);
            inputHt.put("RESIT_TYPE", "1");  // 無此列印條件,因此一律假設為正考
            inputHt.put("CRSNO", totalCrsno);
            inputHt.put("EXAM_TYPE", EXAM_TYPE);            
                               
            this.getAllCrsnoExamDateTime(inputHt, vt2);
            
            Hashtable rot = null;
            /*  舊寫法...取得的考試日期時間有問題
		    com.acer.log.MyLogger logger = new com.acer.log.MyLogger("exa404r");
		    com.acer.db.DBManager dbManager = new com.acer.db.DBManager(logger);
			com.nou.exa.EXAGETEAOE exa = new com.nou.exa.EXAGETEAOE(dbManager);
			
			String b_CRSNO="";
			for (int j = 0; j < vt.size(); j++)
			{
				rot	=	new Hashtable();

		        if(!AYEAR.equals("") || AYEAR==null) {
		            exa.setAYEAR(AYEAR);
		        }
		        if(!SMS.equals("") || SMS==null) {
		            exa.setSMS(SMS);
		        }
		        if(!EXAM_TYPE.equals("") || EXAM_TYPE==null) {
		            exa.setEXAM_TYPE(EXAM_TYPE);
		        }
				exa.setRESIT_TYPE("1");

				b_CRSNO=(String)((Hashtable)vt.get(j)).get("CRSNO");

				if(!(b_CRSNO.equals("")))
				{
					exa.setCRSNO(b_CRSNO);
					exa.mainProcess();
					ArrayList tt= exa.getData();

					if(tt.size()>0)
					{
					//取得考試日期
					String tt1=tt.get(0).toString();
					tt1=tt1.substring(tt1.indexOf("EXAM_DATE"));
					rot.put("EXAM_DATE",tt1.substring((tt1.indexOf("=")+1),((tt1.indexOf("]")))));
					//取得考試起時間
					String tt2=tt.get(0).toString();
					tt2=tt2.substring(tt2.indexOf("EXAM_STIME"));
					rot.put("EXAM_STIME",tt2.substring((tt2.indexOf("=")+1),((tt2.indexOf("]")))));
					//取得考試迄時間
					String tt3=tt.get(0).toString();
					tt3=tt3.substring(tt3.indexOf("EXAM_ETIME"));
					rot.put("EXAM_ETIME",tt3.substring((tt3.indexOf("=")+1),((tt3.indexOf("]")))));
					}
					else
					{
					rot.put("EXAM_DATE","");
					rot.put("EXAM_STIME","");
					rot.put("EXAM_ETIME","");
					}
				}
				vt2.add(rot);
			}
			*/
			// vtData2考試日期時間資料---------------------------------------------------------------結束

			// vtData3學生考試資料---------------------------------------------------------------開始
			//3c. 帶出考試教室學生資料	(該段副程式為宜家所撰)
			//PLAGETEXAMSTDS	取得各中心校區各科考試班級學生名單
			
			//Vector      vt3c  =  new Vector();
			for (int j = 0; j < vt.size(); j++)
			{		    	
	            //將 SQL 清空
	            if (sql.length() > 0)
	                sql.delete(0, sql.length());
	            
	            String	CENTER_ABRCODE=(String)((Hashtable)vt.get(j)).get("CENTER_ABRCODE");
	            String	CCMPS_CODE=(String)((Hashtable)vt.get(j)).get("CMPS_CODE");
	            String	EXAM_CLASSM_CODE=(String)((Hashtable)vt.get(j)).get("CLSSRM_CODE");
	            String	CRSNO=(String)((Hashtable)vt.get(j)).get("CRSNO");
	            String	CLASS_CODE=(String)((Hashtable)vt.get(j)).get("CLASS_CODE");  // 2008.10.17 north 新增regt007條件,不然當遇到同一科不同班時,資料會重複
	            String	SECTION_TEMP =(String)((Hashtable)vt.get(j)).get("SECTION"); // 當不分節次時,印出來的資料會把不同節次的資料印在一起,因此多加此條件
	            
	            sql.append
	            (
	            		" SELECT B.CLSSRM_NAME,A.EXAM_CLASSM_CODE,A.AYEAR,A.SMS,B.CENTER_ABRCODE,B.CMPS_CODE,B.CLSSRM_CODE,A.STNO,C.IDNO AS STD_IDNO,D.NAME AS STD_NAME,"+
	            		" '0' AS SEAT_COL,'0' AS SEAT_ROW, F.CRS_SEQ, G.CRSNO, G.CRS_NAME, (E.CENTER_ABBRNAME) AS CENTER_NAME, H.CMPS_NAME, '1' as ORDER_CLASS, TO_CHAR(A.TUT_CLASS_CODE)||A.CRSNO as STU_CLASS_CODE  "+   
	            		" FROM REGT007 A"+ 
	            		" JOIN STUT003 C ON A.STNO=C.STNO  "+
	            		" JOIN STUT002 D ON C.IDNO=D.IDNO AND C.BIRTHDATE=D.BIRTHDATE"+ 	            	
	            		" JOIN PLAT003 B ON A.AYEAR=B.AYEAR AND A.SMS=B.SMS AND B.CMPS_CODE='"+CCMPS_CODE+"' AND A.EXAM_CLASSM_CODE=B.CLSSRM_CODE  "+ 
	            		" JOIN SYST002 E ON E.CENTER_ABRCODE=B.CENTER_ABRCODE"+
	            		// 取得考試科目序號
	            		" JOIN EXAT019 F ON A.AYEAR=F.AYEAR AND A.SMS=F.SMS AND A.CRSNO=F.CRSNO "+
	            		" JOIN COUT002 G ON G.CRSNO=F.CRSNO "+
	            		// 取得校區名稱
	            		" LEFT JOIN PLAT002 H ON H.AYEAR=B.AYEAR AND H.SMS=B.SMS AND H.CMPS_CODE=B.CMPS_CODE AND H.CENTER_ABRCODE=B.CENTER_ABRCODE "+
	            		" WHERE "+ 
	            		"     A.AYEAR = '"+AYEAR+"'"+  
	            		" AND A.SMS = '"+SMS+"'"+ 
	            		" AND A.EXAM_CLASSM_CODE='"+EXAM_CLASSM_CODE+"'"+
	            		" AND B.CENTER_ABRCODE='"+CENTER_ABRCODE+"'"+ 
	            		" AND A.CRSNO ='"+CRSNO+"'"+
	            		" AND A.TUT_CLASS_CODE='"+CLASS_CODE+"' "+
	            		" AND A.UNQUAL_TAKE_MK = 'N' "+
	            		" AND A.UNTAKECRS_MK = 'N' "+
	            		" AND A.PAYMENT_STATUS <> '1' "+
	            		// 取得寄考生的資料
	            		" UNION "+
	            		" SELECT B.CLSSRM_NAME,A.CLSSRM_CODE as EXAM_CLASSM_CODE,A.AYEAR,A.SMS,B.CENTER_ABRCODE,B.CMPS_CODE,B.CLSSRM_CODE,A.STNO,C.IDNO AS STD_IDNO,D.NAME AS STD_NAME,"+
	            		" '0' AS SEAT_COL,'0' AS SEAT_ROW, F.CRS_SEQ, G.CRSNO, G.CRS_NAME, (E.CENTER_ABBRNAME) AS CENTER_NAME, H.CMPS_NAME, '2' as ORDER_CLASS, '寄考生'||G.CRSNO as STU_CLASS_CODE  "+   
	            		" FROM EXAT026 A"+ 
	            		" JOIN STUT003 C ON A.STNO=C.STNO  "+
	            		" JOIN STUT002 D ON C.IDNO=D.IDNO AND C.BIRTHDATE=D.BIRTHDATE"+ 
	            		" JOIN SYST002 E ON E.CENTER_CODE=A.CENTER_CODE"+
	            		" JOIN PLAT003 B ON A.AYEAR=B.AYEAR AND A.SMS=B.SMS AND E.CENTER_ABRCODE=B.CENTER_ABRCODE  AND B.CMPS_CODE='"+CCMPS_CODE+"' AND A.CLSSRM_CODE=B.CLSSRM_CODE  "+ 
	            		// 取得考試科目序號
	            		" JOIN EXAT019 F ON A.AYEAR=F.AYEAR AND A.SMS=F.SMS AND A.CRSNO=F.CRSNO "+
	            		" JOIN COUT002 G ON G.CRSNO=F.CRSNO "+
	            		// 取得校區名稱
	            		" LEFT JOIN PLAT002 H ON H.AYEAR=B.AYEAR AND H.SMS=B.SMS AND H.CMPS_CODE=B.CMPS_CODE AND H.CENTER_ABRCODE=B.CENTER_ABRCODE "+
	            		" WHERE 0=0 "+ 
	            		" AND A.AYEAR = '"+AYEAR+"' "+  
	            		" AND A.SMS = '"+SMS+"' "+ 
	            		" AND A.EXAM_TYPE = '"+EXAM_TYPE+"' "+
	            		" AND A.EXAM_APP_TYPE = '4' "+
	            		" AND A.CLASS_CODE='"+CLASS_CODE+"' "+
	            		" AND A.CLSSRM_CODE='"+EXAM_CLASSM_CODE+"' "+
	            		" AND A.CENTER_CODE='"+CENTER_CODE+"' "+ 
	            		" AND A.CRSNO ='"+CRSNO+"' "+
	            		" ORDER BY CRSNO,ORDER_CLASS,STNO,CMPS_CODE,CLSSRM_CODE"	            
           		);	
System.out.println("111:"+sql);
                rs1 = dbmanager.getSimpleResultSet(conn);
                rs1.open();	   
                rs1.executeQuery(sql.toString());
	            
                int stuNum = 0; // 該教室中該科的考試人數
                int examType4Num = 0; // 該科寄考生人數
	            while (rs1.next())
	            {
	            	// 計算一般生該班該科的考試人數--不含寄考生
	            	if(rs1.getString("ORDER_CLASS").equals("1"))
	            		stuNum++;
	            	else
	            		examType4Num++;
	                rowHt   =   new Hashtable();

	                // 將欄位抄一份過去
	                for (int i = 1; i <= rs1.getColumnCount(); i++)
	                    rowHt.put(rs1.getColumnName(i), rs1.getString(i));
	                
	                rowHt.put("SECTION", SECTION_TEMP.charAt(0)+"-"+SECTION_TEMP.charAt(1));
	                
	                vt3.add(rowHt);
	            }	            
	            rs1.close();
	            
	            // 因同一科在同一個中心中,一定會在同一個時段考試,故不分校區節次,僅以考試教室+科目來直接取得人數即可
	            returnClassmStuNum.put(EXAM_CLASSM_CODE+CRSNO+CLASS_CODE, stuNum+"");
	            
	            // 如有寄考生,則放入寄考生人數,
	            // 因上面的寄考人數是對應到某班的寄考到這間教室的人數,但同一科有可能多個班的學生寄考到同一間教室,前端一樣僅顯示寄考共多少人,因此有做修改
	            if(examType4Num!=0)
	            	returnClassmStuNum.put(EXAM_CLASSM_CODE+CRSNO+"999999", this.getExamAppType4Stu(ht,(Hashtable)vt.get(j)));
			}
			 //System.out.println("size3:"+vt3.size());
			 // vtData3學生考試資料---------------------------------------------------------------結束
			
			// vtData4考試教室資料---------------------------------------------------------------開始
			//3d.帶出教室座位資料
			String StrC="";
			for (int j = 0; j < vt.size(); j++)
			{
				rs1 = dbmanager.getSimpleResultSet(conn);
                rs1.open();
				StrC="SELECT b.SEAT_COL,b.SEAT_ROW,b.CMPS_CODE,b.CLSSRM_CODE  FROM SYST002 a,PLAT003 b WHERE b.CENTER_ABRCODE=a.CENTER_ABRCODE ";

		        if(!AYEAR.equals("") || AYEAR==null) {
		            StrC+=" AND b.AYEAR='" +AYEAR+ "'";
		        }
		        if(!SMS.equals("") || SMS==null) {
		            StrC+=" AND b.SMS='" +SMS+ "'";
		        }

				String d_CMPS_CODE="";	//校區代號
				String d_CLSSRM_CODE="";	//教室代號
				
				
					
				d_CMPS_CODE=(String)((Hashtable)vt.get(j)).get("CMPS_CODE");
				d_CLSSRM_CODE=(String)((Hashtable)vt.get(j)).get("CLSSRM_CODE");

				StrC += " AND b.CMPS_CODE='" +d_CMPS_CODE+ "'";
				StrC += " AND b.CLSSRM_CODE='" +d_CLSSRM_CODE+ "'";

                rs1.executeQuery(StrC);
                
                if(((Hashtable)vt.get(j)).get("CLSSRM_CODE").toString().equals("1G3001")&&((Hashtable)vt.get(j)).get("CRSNO").toString().equals("100114"))
					System.out.println("StrC:"+StrC);
                
 //System.out.println("exa404("+j+")->"+StrC);
				rot	=	new Hashtable();
				if(rs1.next())
				{
				    for (int k = 1; k <= rs1.getColumnCount(); k++){
				    	String columpTemp = rs1.getString(k);
				    	if(columpTemp==null || columpTemp.trim().length()==0)
				    		columpTemp = "0";
				    	rot.put(rs1.getColumnName(k), columpTemp);
				    }
				    
				    rot.put("CRSNO", ((Hashtable)vt.get(j)).get("CRSNO").toString());
				}

				vt4.add(rot);
				rs1.close();
			}
			// vtData4考試教室資料---------------------------------------------------------------結束		
            
			return result;
        }
        catch(Exception e)
        {
            throw e;
        }
        finally
        {
            if (rs != null)
                rs.close();
            if (rs1 != null)
                rs1.close();
        }
    }

    // 取得所傳入學年期/某些科目的考試日期/時間資料
    public void getAllCrsnoExamDateTime(Hashtable ht, Vector returnVt){
    	DBResult rs = null;
    	try{
    		String ayear = ht.get("AYEAR").toString();
    		String sms = ht.get("SMS").toString();
    		String resitType = ht.get("RESIT_TYPE").toString();
    		String totalCrsno = ht.get("CRSNO").toString();
    		String examType = ht.get("EXAM_TYPE").toString();
    		
    		String getAllCrsnoExamDateTime=
    			"SELECT E21.CRSNO, E21.SECTION, E20.SDATE_WEEK, E20.EDATE_WEEK, E41.STIME AS EXAM_STIME, E41.ETIME AS EXAM_ETIME, " +
    				// 先判斷傳入的科目的考試別,在判斷是要取得起日期或是迄日期作為考試日期(一科僅會在某一天考-->起或迄日期)
    				"DECODE('"+examType+"', '1',DECODE(SUBSTR(E21.SECTION,1,1),E20.SDATE_WEEK,E20.MID_SDATE,E20.MID_EDATE),DECODE(SUBSTR(E21.SECTION,1,1),E20.SDATE_WEEK,E20.FNL_SDATE,E20.FNL_EDATE)) AS  EXAM_DATE " +
    			"FROM EXAT021 E21, EXAT020 E20, EXAT041 E41 " +
    			"WHERE " +    			
    			"    E21.AYEAR='"+ayear+"' "+
    			"AND E21.SMS='"+sms+"' "+
    			"AND E21.RESIT_TYPE='"+resitType+"' "+
    			// 科目可非必輸
    			(totalCrsno.equals("")?"":"AND E21.CRSNO IN ("+totalCrsno+") ")+
    			"AND E41.RESIT_TYPE='"+resitType+"' "+
    			"AND E41.SECTION (+)= SUBSTR(E21.SECTION, 2, 1) " +
    			"AND E20.AYEAR (+)= E21.AYEAR " +
    			"AND E20.SMS (+)= E21.SMS " +
    			"AND E20.RESIT_TYPE (+)= E21.RESIT_TYPE "+
    			"ORDER BY E21.SECTION, E21.CRSNO  ";
    		System.out.println("取得日期:"+getAllCrsnoExamDateTime);
    		rs = dbmanager.getSimpleResultSet(conn);
			rs.open();
			rs.executeQuery(getAllCrsnoExamDateTime);

			while (rs.next()) 
			{
				Hashtable rowHt = new Hashtable();

				for (int i = 1; i <= rs.getColumnCount(); i++) 
					rowHt.put(rs.getColumnName(i), rs.getString(i));

				returnVt.add(rowHt);
			}
			rs.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		if(rs!=null)
    			rs.close();
    	}
    }

	public Vector getDataForPla013m(Hashtable ht) throws Exception
	{
		Vector result = new Vector();
		 
		DBResult rs = null;

		try
		{
			sql.append
			( 
				"SELECT DISTINCT " +
				"A.SECTION, " +
				"B.STIME||'~'||B.ETIME AS TIMES, " +
				"A.SECTION||'　'||B.STIME||'~'||B.ETIME AS SECTION_TIMES  " +
				"FROM EXAT021 A " +
				"JOIN EXAT041 B ON SUBSTR(A.SECTION,2,1) =B.SECTION AND A.RESIT_TYPE=B.RESIT_TYPE " +
				"JOIN REGT013 C ON A.CRSNO=C.CRSNO AND A.AYEAR=C.AYEAR AND A.SMS=C.SMS " +
				"WHERE 1=1 " +
				"AND A.RESIT_TYPE='1' "
			);

			if(!Utility.nullToSpace(ht.get("AYEAR")).equals("")) 
			{
				sql.append("AND A.AYEAR = '" + Utility.nullToSpace(ht.get("AYEAR")) + "' ");
			}

			if(!Utility.nullToSpace(ht.get("SMS")).equals("")) 
			{
				sql.append("AND A.SMS = '" + Utility.nullToSpace(ht.get("SMS")) + "' ");
			}

			sql.append("ORDER BY A.SECTION ");
			
			// 取出所有資料
			rs = dbmanager.getSimpleResultSet(conn);
			rs.open();
			rs.executeQuery(sql.toString());

			Hashtable rowHt = null;

			while (rs.next()) 
			{
				rowHt = new Hashtable();

				/** 將欄位抄一份過去 */
				for (int i = 1; i <= rs.getColumnCount(); i++) 
				{
					rowHt.put(rs.getColumnName(i), rs.getString(i));
				}

				result.add(rowHt);
			}
		}
		catch (Exception e) 
		{
			throw e;
		} 
		finally 
		{
			if(rs != null) 
			{
				rs.close();
			}
		}

		return result;
    }
	
	/*
	 * 取得正(補)考的期中期末考日期與某科目的考試時間
	 */
	public Hashtable getExamDateTime(Hashtable ht) throws Exception
	{
		Hashtable result = new Hashtable();
		
		DBResult rs = null;
		try
		{
			sql.append
			( 
				"select c.STIME,c.ETIME," +
					"decode(b.SDATE_WEEK,substr(a.section,1,1),b.MID_SDATE,b.MID_EDATE) as MID_DATE," +
					"decode(b.SDATE_WEEK,substr(a.section,1,1),b.FNL_SDATE,b.FNL_EDATE) as FNL_DATE " +
				"from exat021 a, exat020 b, exat041 c " +
				"where "+
					"    a.AYEAR='"+ht.get("AYEAR")+"' " +
					"and a.SMS='"+ht.get("SMS")+"' " +
					"and a.RESIT_TYPE='"+ht.get("RESIT_TYPE")+"' " +
					"and a.CRSNO='"+ht.get("CRSNO")+"' " +
					"and b.AYEAR=a.AYEAR " +
					"and b.SMS=a.SMS " +
					"and b.RESIT_TYPE=a.RESIT_TYPE " +
					"and c.RESIT_TYPE=a.RESIT_TYPE " +
					"and c.SECTION=substr(a.section,2,1) "
			);
			
			// 取出所有資料
			rs = dbmanager.getSimpleResultSet(conn);
			rs.open();
			rs.executeQuery(sql.toString());

			if (rs.next()) 
			{
				for (int i = 1; i <= rs.getColumnCount(); i++) 
					result.put(rs.getColumnName(i), rs.getString(i));

				// 判斷期中期末考,以便放入正確的考試日期
				if(ht.get("EXAM_TYPE").toString().equals("1"))
					result.put("EXAM_DATE", rs.getString("MID_DATE"));
				else
					result.put("EXAM_DATE", rs.getString("FNL_DATE"));
			}
		}
		catch (Exception e) 
		{
			throw e;
		} 
		finally 
		{
			if(rs != null) 
			{
				rs.close();
			}
		}

		return result;
    }
	
	/*
	 * 取得寄考生的考試人數(供EXA404R用
	 */
	public String getExamAppType4Stu(Hashtable ht, Hashtable stuData) throws Exception
	{
		String result = "0";
		
		DBResult rs = null;
		try
		{
			String querySql = 			
				"SELECT COUNT(DISTINCT A.STNO) AS STU_COUNT "+
				"FROM exat026 a "+
				"WHERE  "+
				"		a.ayear = '"+ht.get("AYEAR")+"' "+
				"	AND a.sms = '"+ht.get("SMS")+"' "+
				"	AND a.exam_type = '"+ht.get("EXAM_TYPE")+"' "+
				"	AND a.exam_app_type = '4' "+
				"	AND a.clssrm_code = '"+stuData.get("CLSSRM_CODE")+"' "+
				"	AND a.center_code = '"+stuData.get("CENTER_CODE")+"' "+
				"	AND a.crsno = '"+stuData.get("CRSNO")+"' ";
			
			// 取出所有資料
			rs = dbmanager.getSimpleResultSet(conn);
			rs.open();
			rs.executeQuery(querySql);

			if (rs.next()) {
				result=rs.getString("STU_COUNT");
			}
		}
		catch (Exception e) 
		{
			throw e;
		} 
		finally 
		{
			if(rs != null) 
			{
				rs.close();
			}
		}

		return result;
    }
	
	public static void main(String[] args) throws Exception {
        com.acer.log.MyLogger logger = new com.acer.log.MyLogger("PLAGETCLSDATA");
        DBManager dbManager = new DBManager(logger);
        Connection conn	=	dbManager.getConnection(AUTCONNECT.mapConnect("EXA", "0"));
        
        EXAT021GATEWAY getway  = new EXAT021GATEWAY(dbManager, conn);
        
        Vector vtData = new Vector();
        Vector vtData2 = new Vector();
        Vector vtData3 = new Vector();
        Vector vtData4 = new Vector();
        Hashtable stuNum = new Hashtable();
        
        Hashtable requestMap = new Hashtable();
        requestMap.put("AYEAR", "097");
        requestMap.put("SMS", "1");
        requestMap.put("CENTER_CODE", "12");
        requestMap.put("RESIT_TYPE", "2");
        requestMap.put("EXAM_TYPE", "1");
        requestMap.put("CMPS_CODE", "M");

        Vector result = getway.getExat021ForExamTime2(requestMap);

        for(int i=0; i<result.size(); i++){
        	Hashtable content = (Hashtable)result.get(i);
        	System.out.println(content.get("NAME").toString()+content.get("COMBINE_DATA").toString());
        }
        
    }
}
