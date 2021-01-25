package taxi.controller.driver;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import taxi.lib.Injector;
import taxi.model.Driver;
import taxi.service.DriverService;

public class CreateDriverController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("taxi");
    private final DriverService driverService = (DriverService)
            injector.getInstance(DriverService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/driver/create.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("driver_name");
        String license = req.getParameter("licenseNumber");
        String login = req.getParameter("login");
        String password = req.getParameter("pwd");
        String passwordRepeat = req.getParameter("pwdRpt");
        if (password.equals(passwordRepeat)) {
            Driver driver = new Driver(name, license, login, password);
            driverService.create(driver);
            resp.sendRedirect(req.getContextPath() + "/");
        } else {
            req.setAttribute("message", "passwords should be equals");
            req.setAttribute("log", login);
            req.getRequestDispatcher("/WEB-INF/views/driver/create.jsp").forward(req, resp);
        }
    }
}
