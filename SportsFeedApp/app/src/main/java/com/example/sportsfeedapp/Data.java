package com.example.sportsfeedapp;

import com.example.sportsfeedapp.R;
import java.util.ArrayList;
import java.util.List;

public class DataSource {

    private static List<NewsItem> allNews;

    public static List<NewsItem> getAllNews() {
        if (allNews == null) {
            allNews = new ArrayList<>();
            allNews.add(new NewsItem(1,
                    "Champions League Final Set",
                    "Real Madrid face Manchester City in an epic showdown at Wembley. "
                            + "Both sides have fought through tough semi-final encounters to reach "
                            + "the biggest club game in world football. Fans across the globe are "
                            + "anticipating a historic night of drama and goals.",
                    R.drawable.img_football1, "Football", true));

            allNews.add(new NewsItem(2,
                    "NBA Playoffs: Lakers vs Celtics",
                    "LeBron James leads the Los Angeles Lakers to a thrilling Game 7 victory "
                            + "over the Boston Celtics. LeBron posted a 38-point triple-double to keep "
                            + "the Lakers alive in the series. The game came down to a buzzer-beating "
                            + "layup in the final seconds.",
                    R.drawable.img_basketball1, "Basketball", true));

            allNews.add(new NewsItem(3,
                    "Cricket World Cup Highlights",
                    "India beat Australia by 6 wickets in a dramatic World Cup final. "
                            + "Virat Kohli's magnificent century guided India to their target with "
                            + "3 overs to spare. The match drew record television viewership across "
                            + "the subcontinent.",
                    R.drawable.img_cricket1, "Cricket", true));

            allNews.add(new NewsItem(4,
                    "Premier League Weekend Roundup",
                    "Arsenal maintained their lead at the top of the Premier League table "
                            + "after a convincing 3-1 win over Chelsea at the Emirates. Bukayo Saka "
                            + "scored twice to cement his status as one of the league's best players "
                            + "this season.",
                    R.drawable.img_football2, "Football", false));

            allNews.add(new NewsItem(5,
                    "Stephen Curry Breaks Record",
                    "Stephen Curry has officially become the NBA's all-time leader in "
                            + "three-pointers made, surpassing his own previous record. Curry reached "
                            + "the milestone during a home game at the Chase Center, receiving a "
                            + "standing ovation from the sold-out crowd.",
                    R.drawable.img_basketball2, "Basketball", false));

            allNews.add(new NewsItem(6,
                    "Test Series: England vs Pakistan",
                    "England clinched the Test series against Pakistan with a dominant "
                            + "innings victory in the third and final match. Ben Stokes was named "
                            + "Player of the Series for his exceptional all-round performance "
                            + "throughout the three-match campaign.",
                    R.drawable.img_cricket2, "Cricket", false));

            allNews.add(new NewsItem(7,
                    "Transfer Window: Top Deals",
                    "Kylian Mbappe has completed his long-awaited move to Real Madrid in "
                            + "a deal worth over 200 million euros. The French superstar signed a "
                            + "5-year contract and will wear the iconic number 9 shirt at the "
                            + "Bernabeu next season.",
                    R.drawable.img_football3, "Football", false));

            allNews.add(new NewsItem(8,
                    "WNBA Season Preview",
                    "The new WNBA season is set to be the most competitive yet, with "
                            + "several big-name signings reshaping the landscape. The Las Vegas Aces "
                            + "look to defend their championship, while the New York Liberty and "
                            + "Seattle Storm are tipped as the biggest challengers.",
                    R.drawable.img_basketball3, "Basketball", false));

            allNews.add(new NewsItem(9,
                    "IPL 2025 Season Kicks Off",
                    "The Indian Premier League 2025 season has begun with a spectacular "
                            + "opening ceremony in Mumbai. Mumbai Indians defeated Chennai Super Kings "
                            + "in the opening match in front of a packed Wankhede Stadium, setting "
                            + "the tone for what promises to be an exciting tournament.",
                    R.drawable.img_cricket3, "Cricket", false));

            allNews.add(new NewsItem(10,
                    "FIFA World Cup 2026 Preparations",
                    "Host nations USA, Canada and Mexico are ramping up preparations for "
                            + "the 2026 FIFA World Cup. Stadiums across all three countries are "
                            + "undergoing major renovations and upgrades to meet FIFA standards "
                            + "ahead of the biggest sporting event on the planet.",
                    R.drawable.img_football4, "Football", false));
        }
        return allNews;
    }

    public static List<NewsItem> getFeatured() {
        List<NewsItem> list = new ArrayList<>();
        for (NewsItem item : getAllNews()) {
            if (item.isFeatured()) list.add(item);
        }
        return list;
    }

    public static List<NewsItem> getLatest() {
        List<NewsItem> list = new ArrayList<>();
        for (NewsItem item : getAllNews()) {
            if (!item.isFeatured()) list.add(item);
        }
        return list;
    }

    public static List<NewsItem> getByCategory(String category) {
        if (category.equals("All")) return getLatest();
        List<NewsItem> list = new ArrayList<>();
        for (NewsItem item : getLatest()) {
            if (item.getCategory().equals(category)) list.add(item);
        }
        return list;
    }

    public static List<NewsItem> getFeaturedByCategory(String category) {
        if (category.equals("All")) return getFeatured();
        List<NewsItem> list = new ArrayList<>();
        for (NewsItem item : getFeatured()) {
            if (item.getCategory().equals(category)) list.add(item);
        }
        return list;
    }

    public static NewsItem getById(int id) {
        for (NewsItem item : getAllNews()) {
            if (item.getId() == id) return item;
        }
        return null;
    }

    public static List<NewsItem> getRelated(int currentId, String category) {
        List<NewsItem> list = new ArrayList<>();
        for (NewsItem item : getAllNews()) {
            if (item.getCategory().equals(category) && item.getId() != currentId) {
                list.add(item);
            }
        }
        return list;
    }
}