package com.lieharn.jr.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import com.lieharn.jr.core.JumpaRunnerGame;

public class JumpaRunnerGameJava {

  public static void main(String[] args) {
    JavaPlatform.Config config = new JavaPlatform.Config();
    // use config to customize the Java platform, if needed
    JavaPlatform.register(config);
    PlayN.run(new JumpaRunnerGame());
  }
}
