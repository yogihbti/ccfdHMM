# ccfdHMM
Credit Card Fraud Detection using HMM ( Hidden Markow Model)

This project uses HMM (Hidden Markov Model) to recognize fraudulent transactions on credit card.The basic flow of program is mentioned below. 
 
Training Dataset -> Trained Data Model -> Generation of Probability for New Transaction ->Alarm raised if probability below threshold

1.	Training HMM model: While processing the normal card transactions (training dataset), HMM model learns the cardholders spend behavior and adjusts the HMM parameters.

2.	Fraud Detection: New transactions will be tested on existing spend profile of cardholder. It will calculate the probability of new transaction and raise an alarm if the probability is below threshold as it indicates a fraud. 

Note: Hidden Markov Model- is a statistical Markov model with hidden states. HMM model finds application in various temporal pattern recognition such as speech, handwriting, gesture, parts of speech, musical score recognition and bioinformatics. 
