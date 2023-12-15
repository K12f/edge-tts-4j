package io.github.k12f.edgetts4j.samples;

import io.github.k12f.edgetts4j.EdgeTTSFacade;

public class TTSSpeechSample {
    public static void main(String[] args) {
        var builder = EdgeTTSFacade.newBuilder();

        var facade = builder.build();

        var t1 = System.currentTimeMillis();
        var list = facade.listVoice().get();

        System.out.println(list);
        var t2 = System.currentTimeMillis();
        System.out.println(t2 - t1);

    }
}
