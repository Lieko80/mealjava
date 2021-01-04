package org.isa.dal;

import junit.framework.TestCase;
import org.junit.Assert;

import java.sql.SQLException;
import java.util.List;

public class UserDAOTest extends TestCase {

    public void testListAll() throws SQLException {
        List<User> listetest;
        UserDAO repotest = new UserDAO();

        listetest = repotest.ListAll();

        Assert.assertNotNull(listetest);
        Assert.assertTrue(listetest.size() > 0);//or == nb users in db
    }

    public void testFindByEmail() throws SQLException {

        UserDAO repotest = new UserDAO();
        List<User> listetest = repotest.ListAll();

        //ajout en bdd d'un usertest :
        User usertest = new User("testfn", "testln", "testfindbyemail@test.com", "[\"ROLE_USER\"]");
        repotest.addUser(usertest);

        //appel de la fonction testée :
        //je remplis tous les mbs de l'ojet usertest avec les data en base :
        User userbis = repotest.findByEmail(usertest.getEmail());

        //vérification si la fonction marche comme prévu :
        Assert.assertEquals("fonction ne marche pas", "testfn" , userbis.getFirstName());

        //enlever usertest de bdd pour neutraliser effets des tests sur bdd :
        repotest.delete(userbis);

    }

    public void testAddUser() throws SQLException {

        //stocker users dans listetest1 avant ajout:
        UserDAO repotest = new UserDAO();
        List<User> listeAvantAjout = repotest.ListAll();

        //ajout de usertest:
        User usertest = new User("test", "test", "testadd@test.com", "[\"ROLE_USER\"]");
        repotest.addUser(usertest);
        List<User> listeApresAjout = repotest.ListAll();

        //vérifier que listeApres a 1 élément de plus que listeAvant :
        Assert.assertEquals(listeAvantAjout.size()+1, listeApresAjout.size());

        //enlever usertest de bdd pour ne pas créer de modifications à chaque fois que le test se fait:
        //j'ai créé usertest sans id donc j'utilise methode findbyemail pour remplir usertest avec tous les mbs de bdd pour ensuite que delete fonctionne :
        usertest = repotest.findByEmail(usertest.getEmail());
        repotest.delete(usertest);

    }

    public void testUpdate() throws SQLException {

        UserDAO repotest = new UserDAO();

        //ajout en bdd d'un usertest :
        User usertest = new User("testfn", "testln", "testupdate@test.com", "[\"ROLE_USER\"]");
        repotest.addUser(usertest);

        //fill in all members of usertest from db so that usertest.getId works (and delete as well finally) :
        usertest = repotest.findByEmail(usertest.getEmail());

        //make changes :
        usertest.setFirstName("Isabelle");
        usertest.setCity("Test");
        usertest.setZipcode(33333);

        //apply method to be tested :
        repotest.update(usertest);

        //vérification si la méthode marche comme prévu :
        Assert.assertEquals("Isabelle", repotest.find(usertest.getId()).getFirstName());

        //enlever usertest de bdd pour neutraliser effets des tests sur bdd :
        repotest.delete(usertest);
    }

    public void testDelete() throws SQLException {

        //ajout de usertest en bdd :
        UserDAO repotest = new UserDAO();
        User usertest = new User("test", "test", "testdelete@test.com", "[\"ROLE_USER\"]");
        repotest.addUser(usertest);

        //stocker users dans listeAvtDel avant delete:
        List<User> listeAvtDel = repotest.ListAll();

        //fill in all members of usertest from db so that usertest.getId works (and delete as well finally) :
        usertest = repotest.findByEmail(usertest.getEmail());

        //mettre users dans listeApDel après delete de usertest:
        repotest.delete(usertest);
        List<User> listeApDel = repotest.ListAll();

        //vérifier que listeAvtDel a 1 élément de plus que listeApDel :
        Assert.assertEquals((listeApDel.size()+1), listeAvtDel.size());

        //to avoid changes on database :
        //rien à faire puisqu'on a déjà enlevé le usertest qu'on avait inséré

    }
}