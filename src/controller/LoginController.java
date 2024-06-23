/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import javax.swing.JOptionPane;
import model.User;
import model.Home;
import model.Home_Admin;
import view.Login;

/**
 *
 * @author LENOVO
 */
public class LoginController {
    private User model;
    private Login view;
    private Connection connection;

    public LoginController(User model, Login view, Connection connection) {
        this.model = model;
        this.view = view;
        this.connection = connection;

        this.view.setLoginButtonListener(new LoginButtonListener());
    }

    class LoginButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                model.setNIM(Integer.parseInt(view.getNIM()));
                String inputPassword = view.getPassword();
                model.setPassword(inputPassword);

                if (model.authenticate(connection)) {
                    JOptionPane.showMessageDialog(null, "Login berhasil!");
                    if (model.isAdmin()) {
                        new Home_Admin().setVisible(true);
                    } else {
                        new Home(model).setVisible(true);
                    }
                    view.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Login gagal. Periksa kembali NIM dan password Anda.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "NIM harus berupa angka.");
            }
        }
    }
}