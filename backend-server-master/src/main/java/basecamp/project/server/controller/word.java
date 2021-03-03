package basecamp.project.server.controller;

import java.io.Serializable;

public class word implements Serializable{
    public String wo;
    public Float percentage;

    public word(String w, Float p)
    {
        wo= w;
        percentage = p;
    }
}
