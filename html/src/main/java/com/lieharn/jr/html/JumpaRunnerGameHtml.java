package com.lieharn.jr.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import com.lieharn.jr.core.JumpaRunnerGame;

public class JumpaRunnerGameHtml extends HtmlGame {

  @Override
  public void start() {
    HtmlPlatform.Config config = new HtmlPlatform.Config();
    // use config to customize the HTML platform, if needed
    HtmlPlatform platform = HtmlPlatform.register(config);
    platform.assets().setPathPrefix("jumparunner/");
    PlayN.run(new JumpaRunnerGame());
  }
}
