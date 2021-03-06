package com.jollaman999.bubble.component;

import com.jollaman999.bubble.service.BackgroundPlayerService;
import com.jollaman999.bubble.BubbleFrame;
import com.jollaman999.bubble.Moveable;
import com.jollaman999.bubble.state.PlayerWay;
import lombok.Getter;
import lombok.Setter;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.util.ArrayList;
import java.util.List;

// class Player -> new 가능한 애들!! 게임에 존재할 수 있음. (추상 메서드를 가질 수 없다.)
@Getter
@Setter
public class Player extends JLabel implements Moveable {
    private BubbleFrame mContext;
    private List<Bubble> bubbleList;

    // 위치 상태
    private int x;
    private int y;

    // 플레이어의 방향
    private PlayerWay playerWay;

    // 움직임 상태
    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;

    // 벽에 충돌한 상태
    private boolean leftWallCrash;
    private boolean rightWallCrash;

    // 플레이어 이동 속도
    private final int SPEED = 4;
    private final int JUMPSPEED = 2; // up, down

    private ImageIcon playerR, playerL;

    public Player(BubbleFrame mContext) {
        this.mContext = mContext;
        initObject();
        initSetting();
        initBackgroundService();
    }

    private void initObject() {
        playerR = new ImageIcon("image/playerR.png");
        playerL = new ImageIcon("image/playerL.png");
        bubbleList = new ArrayList<>();
    }

    private void initSetting() {
        x = 80;
        y = 535;

        left = false;
        right = false;
        up = false;
        down = false;

        leftWallCrash = false;
        rightWallCrash = false;

        playerWay = PlayerWay.RIGHT;

        setIcon(playerR);
        setSize(50, 50);
        setLocation(x, y);
    }

    private void initBackgroundService() {
        new Thread(new BackgroundPlayerService(this)).start();
    }

    @Override
    public void attack() {
       new Thread(() -> {
           Bubble bubble = new Bubble(mContext);
           mContext.add(bubble);
           bubbleList.add(bubble);
           if (playerWay == PlayerWay.LEFT) {
               bubble.left();
           } else {
               bubble.right();
           }
       }).start();

    }

    // 이벤트 핸들러
    @Override
    public void left() {
        playerWay = PlayerWay.LEFT;
        left = true;
        new Thread(() -> {
            while (left) {
                setIcon(playerL);
                x -= SPEED;
                setLocation(x, y);
                try {
                    Thread.sleep(7);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void right() {
        playerWay = PlayerWay.RIGHT;
        right = true;
        new Thread(() -> {
            while (right) {
                setIcon(playerR);
                x += SPEED;
                setLocation(x, y);
                try {
                    Thread.sleep(7);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    @Override
    public void up() {
        up = true;
        new Thread(() -> {
            for (int i = 0; i < 130/JUMPSPEED; i++) {
                y -= JUMPSPEED;
                setLocation(x, y);
                try {
                    Thread.sleep(4);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            up = false;
            down();

//            y -= 120;
        }).start();
    }

    @Override
    public void down() {
        if (down == false) {

        }

        down = true;
        new Thread(()-> {
            while (down) {
                y += JUMPSPEED;
                setLocation(x, y);
                try {
                    Thread.sleep(4);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            down = false;
        }).start();
    }
}
