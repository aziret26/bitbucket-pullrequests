package controller;

import model.PullRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PullRequestController {
    private String host = "https://api.bitbucket.org/";
    private String resource = "/2.0/repositories/";
    private String subject = "/pullrequests";
    private String repository = "";
    private List<PullRequest> prList;
    private String data;

    /**
     * assigns repository then loads data from it
     * @param repository
     */
    public void setRepository(String repository) {
        this.repository = repository;
        getAllPullRequests();
        if(!this.data.equals(""))
        initPRList();
    }

    public List<PullRequest> getPrList() {
        return prList;
    }

    public String getData() {
        return data;
    }

    private String reqGet(String reqUrl){
        String result = "";
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                result += output;
            }
            conn.disconnect();
        } catch (IOException e) {

//            e.printStackTrace();

            System.out.println("Oops. Seems like repository you've provided doesn't exist.");
        }catch (RuntimeException ex){
            System.out.println("Oops. Seems like repository you've provided doesn't exist.");
        }
        return result;
    }

    private String getAllPullRequests(){
        if(this.repository == null || repository.equals("")){
            return "";
        }
        String url = host + resource + repository + subject;
        data = reqGet(url);
        return data;
    }

    private List<PullRequest> initPRList(){
        String json = data;
        List<PullRequest> prList = new ArrayList<>();
        prList.add(new PullRequest());
        int idIndex,titleIndex,descIndex,linksIndex;

        while(true){
            descIndex = json.indexOf("\"description\"");
            linksIndex = json.indexOf("\"links\"");
            titleIndex = json.indexOf("\"title\"");
            idIndex = json.indexOf("\"id\"");

            if(idIndex == -1 && titleIndex == -1
                    && descIndex == -1 && linksIndex == -1)
                break;
            int operate = 0;
            int index = 0;
            if( descIndex != -1 && ( titleIndex < index || index == 0 ) ){
                operate = 1;
                index = descIndex;
            }
            if( titleIndex != -1 && ( titleIndex < index || index == 0) ){
                operate = 2;
                index = titleIndex;
            }
            if( idIndex != -1 && ( idIndex < index || index == 0 ) ){
                operate = 3;
                index = idIndex;
            }

            PullRequest pr = prList.get(prList.size() - 1);
            String subStr = json.substring(index);
            String[] s = subStr.substring(0,subStr.indexOf(",")).split("\": ");
            json = subStr.substring(subStr.indexOf(","));

            if(operate == 1 ) {
                if (pr.getDescription() != null) {
                    pr = new PullRequest();
                    prList.add(pr);
                }
                pr.setDescription(s[1]);
            }else
            if(operate == 2) {
                if (pr.getTitle() != null) {
                    pr = new PullRequest();
                    prList.add(pr);
                }
                pr.setTitle(s[1]);
            }else
            if( operate == 3 ) {
                if (pr.getId() != null) {
                    pr = new PullRequest();
                    prList.add(pr);
                }
                pr.setId(Integer.parseInt(s[1]));
                pr.setLink("https://bitbucket.org/aziret26/first-repo/pull-requests/"+s[1]);
            }
        }
        this.prList = prList;
        return prList;
    }

    public void printPRList(){
        System.out.println("Repository list:");
        System.out.println("----------");
        if(this.data.equals("")){
            System.out.println("No repository is found");
        }else{
            prList.forEach(pr->System.out.printf("title: %s\nlink:%s\n\n",pr.getTitle(),pr.getLink()));
        }
        System.out.println("----------");
    }
}
