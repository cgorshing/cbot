import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class RobotsManager implements UrlListener
{
  private String agentNameString;

  private UrlManager thePool;

  private UrlListener submitListener;

  private ProgressCallback theProgressCallback;

  private ErrorCallback theErrorCallback;

  /**
   * \brief
   *   Key == domain URL
   *   Value == url of sites that have been visited
   */
  private ArrayList<DomainInfo> seenSites;

  /**
   * \brief 
   *   Key == domain URL
   *   Value == time to wait until next visit, or -1 if no limit
   */
  private Map<String, Integer> crawlDelay;

  private ArrayBlockingQueue<URL> toLookUpSites;

  private Thread worker;

  public RobotsManager(String agentString)
  {
    toLookUpSites = new ArrayBlockingQueue<URL>(100);

    agentNameString = agentString;

    thePool = new UrlManager(this, agentString);

    submitListener = thePool;

    seenSites = new ArrayList<DomainInfo>();

    strikeOffThread();
  }

  private void strikeOffThread()
  {
    worker = new Thread(new Runnable()
    {
      public void run()
      {
        while (true)
        {
          try{Thread.sleep(100);}catch (InterruptedException e){}

          try
          {
            URL url = toLookUpSites.take();

            DomainInfo currDomain = null;

            for (DomainInfo di : seenSites)
            {
              if (di.belongsToHost(url))
              {
                currDomain = di;
                break;
              }
            }

            if (currDomain != null)
            {
              if (currDomain.contains(url) || currDomain.isBlockedPath(url))
                continue;
            }

            submitListener.addUrl(url);
          }
          catch(Exception excep)
          {
            theErrorCallback.error(excep.toString());
          }
        }
      }
    });

    worker.start();
  }

  public void addUrl(URL url)
  {
    theProgressCallback.progress("Adding " + url.toString());

    toLookUpSites.add(url);
  }

  public boolean isDone()
  {
    return false;
  }

  public void setProgressCallback(ProgressCallback progCallback)
  {
    theProgressCallback = progCallback;

    thePool.setProgressCallback(theProgressCallback);
  }

  public void setErrorCallback(ErrorCallback errCallback)
  {
    theErrorCallback = errCallback;

    thePool.setErrorCallback(theErrorCallback);
  }
}
