import java.util.*;
import java.net.*;

public class DomainInfo
{
  private String completeRobotFile;

  private ArrayList<String> disallowPaths;

  private URL host;

  private Date lastRobotRetrieval;

  private LinkedList<URL> allKnownURLs;

  public DomainInfo()
  {
  }

  public boolean isBlockedPath(URL url)
  {
    for (String path : disallowPaths)
    {
      if (url.getPath().startsWith(path))
        return true;
    }

    return false;
  }

  public int getDisAllowTime()
  {
    return 1;
  }

  public Date getLastRobotRetrieve()
  {
    return new Date(System.currentTimeMillis());
  }

  /*
  public String getCompleteRobotFile()
  {
    return completeFile;
  }
  */

  public String getHostName()
  {
    return host.getHost();
  }

  public ArrayList<URL> getKnownUrls()
  {
    return null;
  }

  public boolean contains(URL aUrl)
  {
    return true;
  }

  public void updateRobotFile(String completeFile)
  {
    completeRobotFile = completeFile;

    lastRobotRetrieval = new Date(System.currentTimeMillis());
  }

  public boolean belongsToHost(URL url)
  {
    return host.getHost().equals(url.getHost());
  }

  /**
   * @todo Need to figure out what to do with different queries with the same path
   */
  public void addUrl(URL aUrl) throws Exception
  {
    if (host.getHost().equals(aUrl.getHost()) &&
        host.getProtocol().equals(aUrl.getProtocol()))
    {
      int thisPort = host.getPort();
      int thatPort = aUrl.getPort();

      if (thisPort == -1)
        thisPort = host.getDefaultPort();

      if (thatPort == -1)
        thatPort = aUrl.getDefaultPort();

      if (thisPort == thatPort)
      {
        allKnownURLs.add(aUrl);
      }
      else
      {
        throw new Exception("Adding a URL (" + aUrl.toString() + ") to a DomainInfo which contains the URLs of (" + host.toString() + ")");
      }
    }
    else
    {
      throw new Exception("Adding a URL (" + aUrl.toString() + ") to a DomainInfo which contains the URLs of (" + host.toString() + ")");
    }
  }
}
