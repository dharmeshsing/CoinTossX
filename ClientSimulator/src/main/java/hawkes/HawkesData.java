package hawkes;

/**
 * Created by dharmeshsing on 13/03/16.
 */
public class HawkesData implements Comparable<HawkesData> {
    private long delay;
    private int type;


    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int compareTo(HawkesData hd) {
        return Long.compare(hd.getDelay(),delay);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HawkesData that = (HawkesData) o;

        if (delay != that.delay) return false;
        return type == that.type;

    }

    @Override
    public int hashCode() {
        int result = (int) (delay ^ (delay >>> 32));
        result = 31 * result + type;
        return result;
    }

    @Override
    public String toString() {
        return "HawkesDataPage{" +
                "delay=" + delay +
                ", type=" + type +
                '}';
    }
}
