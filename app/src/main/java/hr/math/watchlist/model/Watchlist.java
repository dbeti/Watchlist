package hr.math.watchlist.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by domagoj on 31.01.16..
 */
@Table(name="Watchlist")
public class Watchlist extends Model{

    @Column(name="name")
    private String name;

    public Watchlist() {
        super();
    }

    public Watchlist(String name) {
        super();

        this.name = name;
    }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
}
