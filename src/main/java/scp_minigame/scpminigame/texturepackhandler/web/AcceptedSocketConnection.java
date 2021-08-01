package scp_minigame.scpminigame.texturepackhandler.web;
import scp_minigame.scpminigame.Main;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

public class AcceptedSocketConnection extends Thread {
    Socket sock;

    Main plugin;

    String DEFAULT_FILE = "index.html";

    public AcceptedSocketConnection(Socket sock, Main plugin) {
        this.sock = sock;
        this.plugin = plugin;
    }

    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        BufferedOutputStream dataOut = null;
        String fileRequested = null;
        try {
            in = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
            out = new PrintWriter(this.sock.getOutputStream());
            dataOut = new BufferedOutputStream(this.sock.getOutputStream());
            String input = in.readLine();
            StringTokenizer parse = new StringTokenizer(input);
            String method = parse.nextToken().toUpperCase();
            fileRequested = parse.nextToken().toLowerCase();
            String contentMimeType = "text/html";
            int counterr = 0, contentLength = 0;
            try {
                String s;
                while (!(s = in.readLine()).equals("")) {
                    if (counterr == 0 && s.equalsIgnoreCase(Main.closeConnection)) {
                        out.close();
                        in.close();
                        this.sock.close();
                        return;
                    }
                    if (s.startsWith("Content-Length: "))
                        contentLength = Integer.parseInt(s.split("Length: ")[1]);
                    counterr++;
                }
            } catch (IOException e) {
                this.plugin.getServer().getLogger().info("This is not an error and should not be reported.");
                this.plugin.getServer().getLogger().info("Counting failed!");
            }
            String finalString = "";
            for (int i = 0; i < contentLength; i++)
                finalString = finalString + (char)in.read();
            if (fileRequested.endsWith("/"))
                fileRequested = fileRequested + this.DEFAULT_FILE;
            File file = new File(this.plugin.getDataFolder() + "/web/", fileRequested);
            int fileLength = (int)file.length();
            String content = getContentType(fileRequested);
            if (method.equals("GET"))
                try {
                    byte[] fileData = readFileData(file, fileLength);
                    out.write("HTTP/1.1 200 OK");
                    out.write("Server: Java HTTP Server from SSaurel : 1.0");
                    out.println("Set-Cookie: Max-Age=0; Secure; HttpOnly");
                    out.println("Date: " + new Date());
                    out.println("Content-type: " + content);
                    out.println();
                    out.flush();
                    dataOut.write(fileData, 0, fileLength);
                    dataOut.flush();
                } catch (IOException e) {
                    this.plugin.getServer().getLogger().info("This is not an error and should not be reported.");
                    this.plugin.getServer().getLogger().info("Writing failed!");
                }
            out.close();
            in.close();
            this.sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];
        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } catch (IOException e) {
            this.plugin.getServer().getLogger().info("This is not an error and should not be reported.");
            this.plugin.getServer().getLogger().info("File: " + file + " Could not be found!");
        } finally {
            if (fileIn != null)
                fileIn.close();
        }
        return fileData;
    }

    private String getContentType(String fileRequested) {
        if (fileRequested.endsWith(".htm") || fileRequested.endsWith(".html"))
            return "text/html";
        if (fileRequested.endsWith(".css"))
            return "text/css";
        if (fileRequested.endsWith(".js"))
            return "application/x-javascript";
        if (fileRequested.endsWith(".svg"))
            return "image/svg+xml";
        return "text/plain";
    }
}