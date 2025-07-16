package course.concurrency.m3_shared.auction;

public class AuctionPessimistic implements Auction {

    private final Notifier notifier;

    private volatile Bid latestBid = new Bid(0L, 0L, 0L);

    public AuctionPessimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    public synchronized boolean propose(Bid bid) {
        if (bid.getPrice() > latestBid.getPrice()) {
            notifier.sendOutdatedMessage(latestBid);
            latestBid = bid;
            return true;
        }
        return false;
    }

    public Bid getLatestBid() {
        return latestBid;
    }
}
