package course.concurrency.m3_shared.auction;

import java.util.concurrent.atomic.AtomicReference;

public class AuctionOptimistic implements Auction {

    private final Notifier notifier;

    private final AtomicReference<Bid> latestBid = new AtomicReference<>(new Bid(0L, 0L, 0L));

    public AuctionOptimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    public boolean propose(Bid bid) {
        Bid curBid;
        do {
            curBid = latestBid.get();
            if (curBid.getPrice() >= bid.getPrice()) {
                return false;
            }
        } while (!latestBid.compareAndSet(curBid, bid));
        notifier.sendOutdatedMessage(curBid);
        return true;
    }

    public Bid getLatestBid() {
        return latestBid.get();
    }
}
