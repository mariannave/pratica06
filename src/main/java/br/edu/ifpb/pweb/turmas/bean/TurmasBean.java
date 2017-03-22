package br.edu.ifpb.pweb.turmas.bean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ActionEvent;

import br.pweb.turmas.dao.AlunoDAO;
import br.pweb.turmas.dao.PersistenceUtil;
import br.pweb.turmas.dao.TurmaDAO;
import br.pweb.turmas.model.Aluno;
import br.pweb.turmas.model.Turma;

@ManagedBean(name = "turmasBean")
@ViewScoped
public class TurmasBean {
	private List<Turma> turmas;
	Turma turma;
	TurmaDAO turmaDao;
	AlunoDAO alunoDao;
	private Long turmaId;
	Aluno aluno;
	private Flash flash;

	@PostConstruct
	public void init() {
		this.turma = new Turma();
		this.aluno = new Aluno();
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public String cadastrar() {
		System.out.print("Cadastrando");
		turmaDao = new TurmaDAO();
		turmaDao.beginTransaction();
		turmaDao.insert(this.turma);
		turmaDao.commit();
		return "turmas?faces-redirect=true";
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public List<Turma> getTurmas() {
		return turmas;
	}

	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
	}

	public void listarTurmas() {
		TurmaDAO tDao = new TurmaDAO();
		this.turmas = tDao.findAll();
	}

	public void atualizarTurmas(ActionEvent e) {
		this.listarTurmas();
	}

	public void selecionar() {
		System.out.println("Chamando nova página");
		turmaDao = new TurmaDAO(PersistenceUtil.getCurrentEntityManager());
		this.turma = turmaDao.find(this.turmaId);
		loadFlash();
	}

	public Long getTurmaId() {
		return turmaId;
	}

	public void setTurmaId(Long turmaId) {
		this.turmaId = turmaId;
	}

	public String matricular() {
		System.out.println("Matriculando Aluno");
		alunoDao = new AlunoDAO();
		turmaDao = new TurmaDAO();
		this.aluno.setTurma(this.turma);
		this.turma.addAluno(this.aluno);

		alunoDao.beginTransaction();
		alunoDao.insert(this.aluno);
		alunoDao.commit();

		turmaDao.beginTransaction();
		turmaDao.update(this.turma);
		turmaDao.commit();

		return String.format("turma?id=%d&faces-redirect=true", this.turma.getId());

	}

	public String excluir(Turma turma) {
		System.out.println("Deletando a turma");
		turmaDao = new TurmaDAO(PersistenceUtil.getCurrentEntityManager());
		turmaDao.beginTransaction();
		turmaDao.delete(turma);
		turmaDao.commit();

		this.turmas.remove(turma);
		return "turmas?faces-redirect=true";
	}

	public String excluirA(Aluno aluno) {
		AlunoDAO adao = new AlunoDAO(PersistenceUtil.getCurrentEntityManager());
		TurmaDAO tdao = new TurmaDAO(PersistenceUtil.getCurrentEntityManager());
		Turma turma = aluno.getTurma();
		
		turma.getAlunos().remove(aluno);
		
		tdao.beginTransaction();
		tdao.update(turma);
		tdao.commit();

		adao.beginTransaction();
		adao.delete(aluno);
		adao.commit();
		
		return String.format("turma?id=%d&faces-redirect=true", this.turma.getId());
	}

	private void loadFlash() {
		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
		flash.put("turma", turma);
		flash.put("turmas", turmas);
	}

	public void unloadFlash() {
		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
		this.setTurma((Turma) flash.get(turma));
		this.setTurmas((List<Turma>) flash.get(turmas));
	}
}
