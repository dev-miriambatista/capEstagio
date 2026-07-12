package View;

import Repository.EmpresaRepository;
import org.example.Model.Empresa;

import javax.swing.*;
import java.awt.*;

public class TelaEmpresa extends JInternalFrame {

    public TelaEmpresa() {

        setTitle("Cadastro Empresa");
        setClosable(true);
        setMaximizable(true);
        setSize(450, 500);
        setLayout(null);

        JPanel painelFundo = Estilo.criarPainelGradiente();
        setContentPane(painelFundo);

        add(Estilo.criarLabelTitulo("Cadastro de Empresa", 30, 10, 300, 30));

        add(Estilo.criarLabel("CNPJ", 30, 50, 130, 25));
        JTextField campoCNPJ = new JTextField();
        campoCNPJ.setBounds(180, 47, 200, 35);
        Estilo.estilizarCampo(campoCNPJ);
        add(campoCNPJ);

        add(Estilo.criarLabel("Razão Social", 30, 98, 130, 25));
        JTextField campoRSocial = new JTextField();
        campoRSocial.setBounds(180, 95, 200, 35);
        Estilo.estilizarCampo(campoRSocial);
        add(campoRSocial);

        add(Estilo.criarLabel("Nome Fantasia", 30, 148, 130, 25));
        JTextField campoNFantasia = new JTextField();
        campoNFantasia.setBounds(180, 145, 200, 35);
        Estilo.estilizarCampo(campoNFantasia);
        add(campoNFantasia);

        add(Estilo.criarLabel("Endereço", 30, 198, 130, 25));
        JTextField campoEndereco = new JTextField();
        campoEndereco.setBounds(180, 195, 200, 35);
        Estilo.estilizarCampo(campoEndereco);
        add(campoEndereco);

        add(Estilo.criarLabel("Telefone", 30, 248, 130, 25));
        JTextField campoTel = new JTextField();
        campoTel.setBounds(180, 245, 200, 35);
        Estilo.estilizarCampo(campoTel);
        add(campoTel);

        add(Estilo.criarLabel("Contato Responsável", 30, 298, 145, 25));
        JTextField campoContato = new JTextField();
        campoContato.setBounds(180, 295, 200, 35);
        Estilo.estilizarCampo(campoContato);
        add(campoContato);

        add(Estilo.criarLabel("Observações", 30, 348, 130, 25));
        JTextField campoObs = new JTextField();
        campoObs.setBounds(180, 345, 200, 35);
        Estilo.estilizarCampo(campoObs);
        add(campoObs);

        JButton botaoSalvar = new JButton("Salvar");
        botaoSalvar.setBounds(165, 415, 120, 40);
        Estilo.estilizarBotao(botaoSalvar);
        add(botaoSalvar);

        botaoSalvar.addActionListener(e -> {

            boolean valido = true;
            JTextField[] camposObrigatorios = {
                    campoCNPJ, campoRSocial, campoNFantasia,
                    campoEndereco, campoTel, campoContato
            };

            for (JTextField campo : camposObrigatorios) {
                if (campo.getText().trim().isEmpty()) {
                    Estilo.aplicarBorda(campo, Estilo.COR_ERRO);
                    valido = false;
                } else {
                    Estilo.aplicarBorda(campo, Estilo.COR_BORDA);
                }
            }

            if (!valido) return;

            if (!Validador.isCNPJValido(campoCNPJ.getText())) {
                JOptionPane.showMessageDialog(null, "O CNPJ introduzido é inválido. Verifique os dígitos.",
                        "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                Estilo.aplicarBorda(campoCNPJ, Estilo.COR_ERRO);
                return;
            }

            // Verificar CNPJ duplicado
            EmpresaRepository empresaRepo = new EmpresaRepository();
            if (empresaRepo.existeCNPJ(campoCNPJ.getText().trim())) {
                JOptionPane.showMessageDialog(null, "Já existe uma empresa cadastrada com este CNPJ.",
                        "CNPJ Duplicado", JOptionPane.WARNING_MESSAGE);
                Estilo.aplicarBorda(campoCNPJ, Estilo.COR_ERRO);
                return;
            }

            Empresa empresa = new Empresa();
            empresa.setCNPJ(campoCNPJ.getText().trim());
            empresa.setrSocial(campoRSocial.getText().trim());
            empresa.setNFantasia(campoNFantasia.getText().trim());
            empresa.setEndereco(campoEndereco.getText().trim());
            empresa.setTel(campoTel.getText().trim());
            empresa.setContato(campoContato.getText().trim());
            empresa.setObs(campoObs.getText().trim());

            try {
                EmpresaRepository.cadastrar(empresa);

                for (JTextField campo : camposObrigatorios) {
                    campo.setText("");
                    Estilo.aplicarBorda(campo, Estilo.COR_BORDA);
                }
                campoObs.setText("");

                JOptionPane.showMessageDialog(null, "Empresa cadastrada com sucesso!", "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro ao cadastrar empresa: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}
