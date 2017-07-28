package junit_testworkspace;
import junit_workspace.EmpBusinessLogic;
import junit_workspace.EmployeeDetails;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by johnny on 2017/7/11.
 */
public class TestEmployeeDetails {
    EmpBusinessLogic empBusinessLogic = new EmpBusinessLogic();
    EmployeeDetails employeeDetails = new EmployeeDetails();

    @Before
    public void setEmployeeDetails () {
        employeeDetails.setName("Rajeev");
        employeeDetails.setMonthlySalary(8000);
        employeeDetails.setAge(25);
    }

    //test to check appraisal
    @Test
    public void testCalculateAppraisal() {
        double appraisal = empBusinessLogic.calculateAppraisal(employeeDetails);
        assertEquals(5000, appraisal, 0.0);
    }

    //test to check yearly salary
    @Test
    public void testCalculateYearlySalary() {
        double salary = empBusinessLogic.calculateYearlySalary(employeeDetails);
        assertEquals(960000, salary, 0.0);
    }
}
