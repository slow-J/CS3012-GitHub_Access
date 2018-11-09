import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

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
      int logInOpt=0;
      System.out.print("Enter 1 for username and password or 2 for OAuth2: ");
      if(scan.hasNext())
        logInOpt = scan.nextInt();
      if(logInOpt==1)
      {
        client = logInUsername();
      }
      else if(logInOpt==2)
      {
        client = logInOauth2();
      }        
      try
      {
        
        System.out.println("Trying authentication for: " + client.getUser());
        UserService userService = new UserService(client);
        User user =  userService.getUser("internaluser");;
       // User user = userService.getUser(client.getUser("internaluser"));
        //System.out.println(user.getName());
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
    // sample repo print
    RepositoryService service = new RepositoryService();
    try
    {
      final int size = 25;
      for (Repository repo : service.getRepositories(client.getUser()))
      {
        

        System.out.println(repo.getName() + " Watchers: " + repo.getWatchers());
        CommitService commitService = new CommitService(client);
        final String message = "   {0} by {1} on {2}";
       
        int pages = 1;
        for (Collection<RepositoryCommit> commits : commitService.pageCommits(repo, size))
        {
          System.out.println("Commit Page " + pages++);
          for (RepositoryCommit commit : commits)
          {
            String sha = commit.getSha().substring(0, 7);
            String author = commit.getCommit().getAuthor().getName();
            Date date = commit.getCommit().getAuthor().getDate();
            System.out.println(MessageFormat.format(message, sha, author, date));
          }
        }
      }
    } catch (IOException e)
    {
      e.printStackTrace();
    }
 
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
    return client;
  }
  //doesnt work
  public static GitHubClient logInOauth2()
  {
    Scanner scan = new Scanner(System.in);
    String authorization = "";
    GitHubClient client = new GitHubClient();
    System.out.print("Enter OAuth2: " );
    if(scan.hasNext())
      authorization = scan.nextLine();
    client.setOAuth2Token("SlAV32hkKG");
    UserService service = new UserService(client);
   
    return client;
  }
  
  
}
