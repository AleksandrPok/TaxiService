package taxi.security;

import java.util.Optional;
import taxi.lib.Inject;
import taxi.lib.Service;
import taxi.model.Driver;
import taxi.service.DriverService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Inject
    private DriverService driverService;

    @Override
    public Driver login(String login, String password) throws AuthenticationException {
        Optional<Driver> driverByLogin = driverService.findByLogin(login);
        if (driverByLogin.isPresent() && driverByLogin.get().getPassword().equals(password)) {
            return driverByLogin.get();
        }
        throw new AuthenticationException("Incorrect login or password");
    }
}
