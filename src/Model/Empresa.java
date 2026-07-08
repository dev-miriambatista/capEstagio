package Model;

public class Empresa {
 private String CNPJ, rSocial, NFantasia, end, tel, contato;

 public Empresa(
         String CNPJ,
         String rSocial,
         String NFantasia,
         String end,
         String tel,
         String contato
 ) {
  this.CNPJ = CNPJ;
  this.rSocial = rSocial;
  this.NFantasia = NFantasia;
  this.end = end;
  this.tel = tel;
  this.contato = contato;
 }

 public String getCNPJ() {
  return CNPJ;
 }

 public void setCNPJ(String CNPJ) {
  this.CNPJ = CNPJ;
 }

 public String getrSocial() {
  return rSocial;
 }

 public void setrSocial(String rSocial) {
  this.rSocial = rSocial;
 }

 public String getNFantasia() {
  return NFantasia;
 }

 public void setNFantasia(String NFantasia) {
  this.NFantasia = NFantasia;
 }

 public String getEnd() {
  return end;
 }

 public void setEnd(String end) {
  this.end = end;
 }

 public String getTel() {
  return tel;
 }

 public void setTel(String tel) {
  this.tel = tel;
 }

 public String getContato() {
  return contato;
 }

 public void setContato(String contato) {
  this.contato = contato;
 }
}