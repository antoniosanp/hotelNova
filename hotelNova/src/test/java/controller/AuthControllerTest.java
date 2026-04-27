package controller;

import model.User;
import org.junit.jupiter.api.Test;
import services.interfaces.IAuthService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthControllerTest {

    @Test
    void loginDebeRetornarUsuarioCuandoCredencialesSonValidas() {
        AuthController controller = new AuthController(new FakeAuthService());

        User logged = controller.login("admin@mail.com", "123456");

        assertEquals("admin@mail.com", logged.getEmail());
        assertEquals("ADMIN", logged.getRol());
    }

    @Test
    void loginDebeLanzarExcepcionCuandoCredencialesInvalidas() {
        AuthController controller = new AuthController(new FakeAuthService());

        assertThrows(RuntimeException.class, () -> controller.login("admin@mail.com", "bad-password"));
    }

    private static class FakeAuthService implements IAuthService {

        @Override
        public User register(String email, String plainPassword, String rol, String name) {
            return new User(1, email, "hashed", rol, name);
        }

        @Override
        public User login(String email, String plainPassword) {
            if ("admin@mail.com".equals(email) && "123456".equals(plainPassword)) {
                return new User(1, email, "hashed", "ADMIN", "Admin");
            }
            throw new RuntimeException("Credenciales inválidas");
        }
    }
}
