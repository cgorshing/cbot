import java.util.*;
import java.net.*;

public class UrlTask implements Runnable
{
  private String agentName;

  private ArrayList<URL> sitesToVisit;

  private ProgressCallback theProgCallback;

  private ErrorCallback theErrCallback;

  private UrlListener theUrlCallback;

  public UrlTask(String agentString, ArrayList<URL> Urls, UrlListener urlCallback )
  {
    agentName = agentString;

    sitesToVisit = Urls;

    theUrlCallback = urlCallback;
  }

  public UrlTask(String agentString,
                  ArrayList<URL> Urls,
                  UrlListener urlCallback,
                  ProgressCallback progCallback,
                  ErrorCallback errCallback)
  {
    agentName = agentString;

    sitesToVisit = Urls;

    theUrlCallback = urlCallback;

    theProgCallback = progCallback;

    theErrCallback = errCallback;
  }

  public void run()
  {
    try
    {
      theProgCallback.progress("UrlTask.run - passing agentName: " + agentName);

      HTMLParser parser = new HTMLParser(agentName);

      for (URL aUrl : sitesToVisit)
      {
        parser.feed(aUrl);

        List<URL> list = parser.getLinks();

        for (URL link : list)
        {
          theUrlCallback.addUrl(link);

          sendProgress(link.toString());
        }
      }
    }
    catch (Exception excep)
    {
      sendError(excep.toString());
    }

    sendProgress("Made it here inside UrlTask.run()");
  }

  public void setProgressCallback(ProgressCallback progCallback)
  {
    theProgCallback = progCallback;
  }

  public void setErrorCallback(ErrorCallback errCallback)
  {
    theErrCallback = errCallback;
  }

  private void sendProgress(String message)
  {
    if (theProgCallback != null)
      theProgCallback.progress(message);
  }

  private void sendError(String message)
  {
    if (theErrCallback != null)
      theErrCallback.error(message);
  }
}
