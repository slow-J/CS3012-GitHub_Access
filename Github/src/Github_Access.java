import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.GitHubRequest;
import org.eclipse.egit.github.core.client.GitHubResponse;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;

/**
 * 
 */

/**
 * @author Jakub
 *
 */
public class Github_Access 
{

	/**
	 * Sample basic authentication
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException 
	{
	  String username = "";
	  String pass = "";
		boolean exit = false;
		GitHubClient client = new GitHubClient();
		Scanner scan = new Scanner(System.in);
    while (exit == false)
    {     

      exit = true;
      System.out.print("Enter username: ");
      username = scan.nextLine();
      System.out.print("Enter password: ");
      pass = scan.nextLine();
      try
      {
        client.setCredentials(username, pass);
        System.out.println("Trying authentication for: "+ client.getUser());
        UserService userService = new UserService(client);
        User user = userService.getUser(username);
        System.out.println(userService.getUser(username));
      } 
      catch (RequestException re)
      {
        if (re.getMessage().endsWith("Bad credentials (401)"))
        {
          exit=false;
        }
      }
    }
    scan.close();
    //sample repo print
		RepositoryService service = new RepositoryService();
    try
    {
      for (Repository repo : service.getRepositories(client.getUser()))
      {
        System.out.println(repo.getName() + " Watchers: " + repo.getWatchers());
      }
    } catch (IOException e)
    {
      e.printStackTrace();
    }
	}

}
