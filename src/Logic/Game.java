package Logic;

import Network.*;
import sample.Controller;

/**
 * Created by Martyna on 12.05.2017.
 */
public class Game {
    private GameStage stage=GameStage.START;
    public static final int BOARDHIGHT = 15;
    public static final int BOARDWIDTH = 15;
    private Board board;
    private int shipNo = 5;
    private int score = 0;
    private NetworkManager netManager;
    boolean isYourTurn;
    Controller controller;

    public Game (Boolean isHost, int portNo, String serverIP,Controller controller){
        board=new Board(BOARDHIGHT,BOARDWIDTH);
        netManager=new NetworkManager(this, isHost, portNo, serverIP);
        netManager.run();
        this.controller=controller;
    }

    public GameStage getStage(){
        return stage;
    }

    public void setStage(GameStage stage){
        this.stage=stage;
    }

    public boolean isYourTurn() {
        return  isYourTurn;
    }
    public void takeHit(int x, int y)throws Exception{
        if (board.takeHit(x, y) ){
            if(board.isShipFloating(x, y))
                netManager.sendMessage(Message.getHitNotSunkMessage(x,y));
            else
                netManager.sendMessage(Message.getHitAndSunkMessage(x,y));
        }
        else {
            netManager.sendMessage(Message.getMissMessage() );
            isYourTurn=true;
        }
    }
    public void main(String[] args){
        netManager.run();
        //placeShips();

        playRound();
    }
    public boolean playRound(){
       //debug
  return true;
    }
    public void handleMessage(Message message)throws Exception{

        switch (message.getType()){
            case ATTACK:
                takeHit(message.getX(), message.getY() );
                break;
            case MISS:
                isYourTurn=false;
                break;
            case SHIPHIT:
                if(message.getFloating()==true)
                    controller.setShipHit( message.getX(), message.getY() );
                else
                    controller.setShipSunkHit( message.getX(), message.getY() );
                break;
            case GAME_END:
                controller.printMessage("wygrałeś");
                break;
        }
        return;
    }
    public void attackField(int x, int y) {
        netManager.sendMessage(Message.getAttackMessage(x, y));
    }
}
