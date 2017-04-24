/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clzola.pmnlgame.message;

/**
 *
 * @author Lazar
 */
public class AuthServerMessages {
    public static class AuthMessage { public String username, password; }
    public static class AuthAcceptMessage { }
    public static class AuthFailedMessage { public String error; }
}