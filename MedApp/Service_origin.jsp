<%@page import="com.sun.jimi.core.Jimi"%>
<%@page import="com.sun.jimi.core.JimiUtils"%>
<%@page import="com.sun.jimi.core.util.JimiUtil"%>
<%@page import="java.awt.RenderingHints"%>
<%@page import="java.awt.Color"%>
<%@page import="java.awt.Graphics2D"%>
<%@page import="java.awt.image.BufferedImage"%>
<%@page import="java.awt.Image"%>
<%@page import="javax.imageio.ImageIO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>

<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="org.apache.commons.fileupload.*"%>
<%@ page import="org.apache.commons.fileupload.disk.*"%>
<%@ page import="org.apache.commons.fileupload.servlet.*"%>
<%@ page import="org.apache.commons.io.output.*"%>


<%
//   String command = "curl -X POST http://127.0.0.1:8881/models/medcamera/v1/predict -H \"Content-Type:multipart/form-data\" -F \"data={\\\"key\\\": \\\"Filename\\\"};type=application/json\" -F \"Filename=@";            
//   out.println(command);
   File file;

   int maxFileSize = 5000 * 1024;

   int maxMemSize = 5000 * 1024;

   ServletContext context = pageContext.getServletContext();
   String filePath = "/usr/share/tomcat8/webapps/ROOT/MedApp/img/";
   File temporaryDir = new File(config.getServletContext().getRealPath("/temp/"));
   String contentType = request.getContentType();

   if (contentType != null && (contentType.indexOf("multipart/form-data") >= 0)) {
      DiskFileItemFactory factory = new DiskFileItemFactory();

      factory.setSizeThreshold(maxMemSize);
      factory.setRepository(temporaryDir);
      ServletFileUpload upload = new ServletFileUpload(factory);
      upload.setSizeMax(maxFileSize);

      try {
         List fileItems = upload.parseRequest(request);
         Iterator i = fileItems.iterator();

         while (i.hasNext())
         {
            FileItem fi = (FileItem) i.next();

            if (!fi.isFormField()) {
               String fieldName = fi.getFieldName();
               String fileName = fi.getName();
               boolean isInMemory = fi.isInMemory();
               long sizeInBytes = fi.getSize();

               if (fileName.lastIndexOf("\\") >= 0) {
                  file = new File(filePath + fileName.substring(fileName.lastIndexOf("\\")));
               } else {
                  file = new File(filePath + fileName.substring(fileName.lastIndexOf("\\") + 1));
               }

               fi.write(file);

               String imagebaam = file.getAbsolutePath();
               try {
                  Image imagebaaam = JimiUtils.getThumbnail(imagebaam, 64,65, Jimi.IN_MEMORY);
                  Jimi.putImage(imagebaaam, imagebaam);
               } catch (Exception e) {
                  e.printStackTrace();
               }

               Process process = null;
               BufferedReader in = null;
               BufferedReader err = null;
		
               try {
                  String command = "curl -X POST http://127.0.0.1:8881/models/MedCamera1.001/v1/predict -H \"Content-Type:multipart/form-data\" -F \"data={\\\"key\\\": \\\"Image\\\"};type=application/json\" -F Image=@"+imagebaam;
		  String s;
                  process = Runtime.getRuntime().exec(command);

                  in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                  String result = "";
                  while ((s = in.readLine()) != null)
                     result += s;

                  out.print(result);
                 
                  err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                  while (err.ready())
                     out.println(err.readLine() + "<br>");
               } catch (Exception e) {
                  out.println("Error : " + e);
               } finally {
                  if (in != null)
                     try {
                        in.close();
                     } catch (Exception sube) {
                     }
                  if (err != null)
                     try {
                        err.close();
                     } catch (Exception sube) {
                     }
               }

            }
         }
      } catch (Exception ex) {
         ex.printStackTrace();
      }

   } else {

   }
%>
