import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Scanner;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;



/**
 * @author Jakub
 *
 */
public class Github_Access
{
  
  public static void main(String[] args) throws IOException
  {
    boolean exit = false;
    GitHubClient client = new GitHubClient();  
    Scanner scan = new Scanner(System.in);
    while (exit == false)
    {
      exit = true;
      client = logInUsername();
          
      try
      {
        
        System.out.println("Trying authentication for: " + client.getUser());
        UserService userService = new UserService(client);
        @SuppressWarnings("unused")
        User user =  userService.getUser("internaluser");
       
      } catch (RequestException re)
      {
        if (re.getMessage().endsWith("Bad credentials (401)"))
        {
          System.out.println("Bad credentials (401)");
          exit = false;
        }
      } 
    }
    scan.close();
    
    JsonObjectBuilder json = Json.createObjectBuilder();
    JsonObjectBuilder json1 = Json.createObjectBuilder();
    JsonObjectBuilder json2;
    JsonObjectBuilder json3;
    RepositoryService service = new RepositoryService();
    try
    {
      final int size = 25;
      for (Repository repo : service.getRepositories(client.getUser()))
      {
        json2 = Json.createObjectBuilder();
        CommitService commitService = new CommitService(client);
        for (Collection<RepositoryCommit> commits : commitService.pageCommits(repo, size))
        {
          int comC = 0;
          json3 = Json.createObjectBuilder();
          for (RepositoryCommit commit : commits)
          {
            comC++;
            json3.add("sha", commit.getSha().substring(0, 7));
            json3.add("author",  commit.getCommit().getAuthor().getName());
            json3.add("date", ""+ commit.getCommit().getAuthor().getDate());
            json2.add("commit "+comC, json3.build());  
          }         
          
        }
        json1.add("reponame", repo.getName());
        json1.add("commits", json2.build());
        
      }
      json.add("Repos", json1.build());
    } catch (IOException e)
    {
      System.out.println("Error");
      e.printStackTrace();
    }
   
    json.toString();
    Writer writer = new FileWriter("Output.json");
    writer.write(json.build().toString());
    writer.close();
    System.out.println("JSON made");
  }
  public static GitHubClient logInUsername()
  {
    Scanner scan = new Scanner(System.in);
    String username = "";
    String pass = "";
    GitHubClient client = new GitHubClient();
    System.out.print("Enter username: ");
    username = scan.nextLine();
    System.out.print("Enter password: ");
    pass = scan.nextLine();
    client.setCredentials(username, pass); 
    scan.close();
    return client;
  }  
  
}