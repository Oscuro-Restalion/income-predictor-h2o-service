package oscuroweb.ia.service.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import hex.genmodel.MojoModel;
import hex.genmodel.easy.EasyPredictModelWrapper;
import hex.genmodel.easy.EasyPredictModelWrapper.Config;
import hex.genmodel.easy.RowData;
import hex.genmodel.easy.exception.PredictException;
import hex.genmodel.easy.prediction.BinomialModelPrediction;
import lombok.extern.slf4j.Slf4j;
import oscuroweb.ia.dto.IncomeDto;
import oscuroweb.ia.dto.OutputDto;
import oscuroweb.ia.dto.OutputResultDto;
import oscuroweb.ia.service.H2OService;

@Slf4j
@Service
public class H2OServiceImpl implements H2OService {

	@Value("${model.path}")
	private String modelPath;

	@Override
	public OutputDto evaluate(IncomeDto input) {
		try {

			Config config = new Config();
			config.setConvertInvalidNumbersToNa(true);
			config.setConvertUnknownCategoricalLevelsToNa(true);
			config.setUseExtendedOutput(true);
			config.setModel(MojoModel.load(modelPath));

			EasyPredictModelWrapper model = new EasyPredictModelWrapper(config);

			RowData row = new RowData();
			row.put("C1", input.getAge());
			row.put("C2", input.getWorkclass());
			row.put("C3", input.getFnlwgt());
			row.put("C4", input.getEducation());
			row.put("C5", input.getEducationNum());
			row.put("C6", input.getMaritalStatus());
			row.put("C7", input.getOccupation());
			row.put("C8", input.getRelationship());
			row.put("C9", input.getRace());
			row.put("C10", input.getSex());
			row.put("C11", input.getCapitalGain());
			row.put("C12", input.getCapitalLoss());
			row.put("C13", input.getHoursPerWeek());
			row.put("C14", input.getNativeCountry());

			BinomialModelPrediction p = model.predictBinomial(row);
			log.info("Prediction instance: {}", p.classProbabilities);

			OutputDto output = OutputDto.builder()
					.label(p.label)
					.build();

			log.info(output.toString());

			return output;

		} catch (IOException e) {
			log.error("Unable to find H2O model zip file '{}'", modelPath);
			return OutputDto.builder().build();
		} catch (PredictException e) {
			log.error("An error ocurred running model '{}' :: {}", e.getMessage());
			return OutputDto.builder().build();
		}
	}

	@Override
	public OutputResultDto addResult(IncomeDto input) {
		return OutputResultDto.builder().updated(Boolean.TRUE).build();
	}

}
