<%@page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.qzsitu.stourway.domain.ProcessHistoryRecord" %>

    <table class="table">
      <thead><tr><th><h4>历史记录</h4></th><th>留言/附件</th><th>操作者</th><th>决策</th></tr></thead>
      <tbody>
    <% 
      List<ProcessHistoryRecord> phrList = (List<ProcessHistoryRecord>)request.getAttribute("historyRecord"); 

        for(int i = 0; i < phrList.size(); i++) {
        	ProcessHistoryRecord record = (ProcessHistoryRecord) phrList.get(i);
    %>
          <tr>
            <td><%=(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(record.getDatetime())) %></td>
            <td>
              <%=record.getComment() %><br>
              <% 
                String[] attachments = record.getAttachmentIds().split("\\|");
                for(int j = 0; j < attachments.length; j++) {
                  if(attachments[j].indexOf(':') == -1) continue;
              %>
                 <a href="/file/download/<%=attachments[j].split(":")[0] %>"><%=attachments[j].split(":")[1] %></a>
              <%
                }
              %>
            </td>
            <td><%=record.getUser() %></td>
            <td><%=record.getDecision() %></td>
          </tr>
    <%
        } 
    %>
      </tbody>
    </table>