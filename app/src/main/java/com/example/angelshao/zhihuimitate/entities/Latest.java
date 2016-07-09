package com.example.angelshao.zhihuimitate.entities;

import java.util.List;

/**
 * Created by David-Ann on 2016/7/8 0008.
 */
public class Latest {

    private String date;
    private List<StoriesEntity> stories;
    private List<TopStoriesEntity> top_stories;

    public void setDate(String date) {
        this.date = date;
    }

    public void setStories(List<StoriesEntity> stories) {
        this.stories = stories;
    }

    public void setTop_stories(List<TopStoriesEntity> top_stories) {
        this.top_stories = top_stories;
    }

    public String getDate() {
        return date;
    }

    public List<StoriesEntity> getStories() {
        return stories;
    }

    public List<TopStoriesEntity> getTop_stories() {
        return top_stories;
    }

    public static class TopStoriesEntity {
        /**
         * image : http://pic1.zhimg.com/00b1defa1331cc1e6cd1a14239907d04.jpg
         * type : 0
         * id : 7326480
         * ga_prefix : 102119
         * title : 看完《星球大战》新预告片超激动，更重要的是完全没有剧透
         */

        private String image;
        private int type;
        private int id;
        private String ga_prefix;
        private String title;

        public void setImage(String image) {
            this.image = image;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public int getType() {
            return type;
        }

        public int getId() {
            return id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public String getTitle() {
            return title;
        }
    }
}
