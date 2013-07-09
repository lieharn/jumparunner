package com.lieharn.jr.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import com.lieharn.jr.core.JumpaRunnerGame;

public class JumpaRunnerGameActivity extends GameActivity {

  @Override
  public void main(){
    PlayN.run(new JumpaRunnerGame());
  }
}
