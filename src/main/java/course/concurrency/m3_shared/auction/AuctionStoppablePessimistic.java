package course.concurrency.m3_shared.auction;

public class AuctionStoppablePessimistic implements AuctionStoppable {

    private final Notifier notifier;

    private volatile Bid latestBid = new Bid(0L, 0L, 0L);

    private boolean isStopped = false;

    public AuctionStoppablePessimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    public synchronized boolean propose(Bid bid) {
        if (!isStopped && bid.getPrice() > latestBid.getPrice()) {
            notifier.sendOutdatedMessage(latestBid);
            latestBid = bid;
            return true;
        }
        return false;
    }

    public Bid getLatestBid() {
        return latestBid;
    }

    public synchronized Bid stopAuction() {
        isStopped = true;
        return latestBid;
    }
}
