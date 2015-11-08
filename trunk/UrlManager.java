import java.util.*;
import java.net.*;
import java.util.concurrent.*;

public class UrlManager extends ThreadPoolExecutor implements UrlListener
{
  public static int CorePoolSize = 5;

  public static int MaxPoolSize = 25;

  public static long KeepAliveTime = 5;

  public static TimeUnit KeepAliveUnit = TimeUnit.SECONDS;

  private String agentNameString;

  private ProgressCallback theProgressCallback;

  private ErrorCallback theErrorCallback;

  private UrlListener taskUrlListener;

  public UrlManager(UrlListener submitUrlListener, String agentString)
  {
    super(CorePoolSize,
        MaxPoolSize,
        KeepAliveTime,
        KeepAliveUnit,
        new ArrayBlockingQueue<Runnable>(5000, true));

    agentNameString = agentString;

    taskUrlListener = submitUrlListener;
  }

  public void addUrl(URL aUrl)
  {
    ArrayList<URL> list = new ArrayList<URL>();

    list.add(aUrl);

    UrlTask task = new UrlTask(agentNameString,
        list,
        taskUrlListener,
        theProgressCallback,
        theErrorCallback);

    super.submit(task);
  }

  public void setProgressCallback(ProgressCallback progCallback)
  {
    theProgressCallback = progCallback;
  }

  public void setErrorCallback(ErrorCallback errCallback)
  {
    theErrorCallback = errCallback;
  }

  protected void afterExecute(Runnable r, Throwable t)
  {
    if (theProgressCallback != null)
      theProgressCallback.progress("Thread Completed Task");
  }
}
