import java.io.*;
import java.net.*;
import java.util.*;

public class MainEntry implements ProgressCallback, ErrorCallback, Runnable
{
  private String [] arguments;

  public MainEntry(String [] args)
  {
    arguments = args;
  }

  public static void main(String [] args)
  {
    try
    {
      Thread worker = new Thread(new MainEntry(args));

      worker.start();
    }
    catch (Exception excep)
    {
      excep.printStackTrace();
    }

    System.out.println("MainEntry.main ending");
  }

  public void run()
  {
    try
    {
      RobotsManager manager = new RobotsManager("cbot/0.1 (http://dev.gorshing.net/cbot.php)");

      manager.setProgressCallback(this);

      manager.setErrorCallback(this);

      if (arguments.length == 0)
      {
        System.out.println("Please enter a URL as a starting point: ");

        BufferedReader stdInReader = new BufferedReader(new InputStreamReader(
              System.in));

        String input = stdInReader.readLine();

        StringTokenizer tokens = new StringTokenizer(input, " ");

        while (tokens.hasMoreTokens())
        {
          URL startingPoint = new URL(tokens.nextToken());

          System.out.println("Using " + startingPoint.toString() + " as the starting point");

          manager.addUrl(startingPoint);
        }
      }
      else
      {
        for (String arg : arguments)
        {
          URL aStartingPoint = new URL(arg);

          System.out.println("Adding " + aStartingPoint.toString() + " as a starting point");

          manager.addUrl(aStartingPoint);
        }
      }

      /*
      while (!manager.isDone())
      {
        try{ Thread.sleep(500);}catch (Exception e) {}
      }
      */
    }
    catch (Exception excep)
    {
      excep.printStackTrace();
    }
  }

  public int progress(String message)
  {
    System.out.println(message);
    System.out.flush();

    return 0;
  }

  public int error(String message)
  {
    System.err.println(message);
    System.err.flush();

    return 0;
  }
}
