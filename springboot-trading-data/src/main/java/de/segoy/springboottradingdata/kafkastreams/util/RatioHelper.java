package de.segoy.springboottradingdata.kafkastreams.util;

import org.springframework.stereotype.Service;

@Service
public class RatioHelper {

    public Ratios getRatio(int aggregated, int received) {
        final int gcd = gcd(aggregated, received);

        return new Ratios(gcd, aggregated/gcd, received/gcd);
    }

    //Greatest Common Divisor, Euklid Formula
    private int gcd(int p, int q) {
        if (q == 0) return p;
        else return gcd(q, p % q);
    }

    public record Ratios(Integer gcd, Integer aggregated, Integer received){}
}
