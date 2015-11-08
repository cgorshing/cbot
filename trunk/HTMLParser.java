import java.util.*;
import java.net.*;
import java.io.*;

import org.htmlparser.*;
import org.htmlparser.tags.*;
import org.htmlparser.util.*;
import org.htmlparser.filters.*;

public class HTMLParser
{
  /**
   * \brief
   */
  private String userAgent;

  /**
   */
  private List<URL> linkList;

  /**
   */
  public HTMLParser()
  {
    init();
  }

  /**
   */
  public HTMLParser(String userAgent)
  {
    init();
    this.userAgent = userAgent;
  }

  /**
   */
  private void init()
  {
    linkList = new ArrayList<URL>();
  }

  /**
   */
  public void feed(URL url)
  {
    try
    {
      NodeList list = getFile(url);

      for (int i = 0; i < list.size(); ++i)
      {
        LinkTag node = (LinkTag)list.elementAt(i);

        if (node.isHTTPLink())
        {
          String relName = node.getAttribute("rel");

          if (relName == null || !relName.equalsIgnoreCase("nofollow"))
            linkList.add(new URL(node.extractLink()));
          else
          {
            System.out.println("received a nofollow");
          }
        }
      }
    }
    catch(Exception excep)
    {
      System.out.println(excep.toString());
    }
  }

  /**
   */
  private NodeList getFile(URL aUrl)
    throws IOException, MalformedURLException, ParserException
  {
    URLConnection conn = aUrl.openConnection();

    if (userAgent != null)
      conn.addRequestProperty("User-Agent", userAgent);

    Parser parser = new Parser(conn);

    return parser.extractAllNodesThatMatch(new TagNameFilter("A"));
  }

  /**
   */
  public List<URL> getLinks()
  {
    return linkList;
  }
}
